package com.mealtoyou.alarmservice.infrastructure.kafka;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestProcessor {
	private final ConsumerFactory<String, String> consumerFactory;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final KafkaTopicUtils kafkaTopicUtils;
	private final ApplicationContext applicationContext;
	private final List<ConcurrentMessageListenerContainer<String, String>> containers = new ArrayList<>();
	private final KafkaProxyCreator kafkaProxyCreator;
	private final ObjectMapper objectMapper;

	private void fluxConvertAndSend(Flux<?> fluxResult, KafkaKey key) {
		fluxResult.collectList().subscribe(
			item -> {
				try {
					kafkaTemplate.send(key.getKafkaResponseTopic(), key.getUuid(),
						objectMapper.writeValueAsString(item));
				} catch (JsonProcessingException e) {
					log.error("JSON formatting error for key: {} with item: {}", key, item, e);
				}
			},
			error -> log.error("Error processing the Mono for key: {}", key, error));
	}

	private void monoConvertAndSend(Mono<?> monoResult, KafkaKey key) {
		monoResult.subscribe(
			item -> {
				try {
					String message = objectMapper.writeValueAsString(item);
					kafkaTemplate.send(key.getKafkaResponseTopic(), key.getUuid(), message);
				} catch (JsonProcessingException e) {
					log.error("JSON formatting error for key: {} with item: {}", key, item, e);
				}
			},
			error -> log.error("Error processing the Mono for key: {}", key, error));
	}

	private void registerListener(Method method, Object service) {
		KafkaMessageListener config = method.getAnnotation(KafkaMessageListener.class);
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.setConcurrency(1);
		ConcurrentMessageListenerContainer<String, String> container = factory.createContainer(
			kafkaTopicUtils.getRequestTopic(config.topic()));

		container.setupMessageListener((MessageListener<String, String>)record -> {
			String message = record.value();
			String key = record.key();
			try {
				Object result = method.invoke(service, message);
				KafkaKey kafkaKey = objectMapper.readValue(key, KafkaKey.class);
				if (result instanceof String stringResult) {
					kafkaTemplate.send(kafkaKey.getKafkaResponseTopic(), kafkaKey.getUuid(), stringResult);
				} else if (result instanceof Flux<?> fluxResult) {
					fluxConvertAndSend(fluxResult, kafkaKey);
				} else if (result instanceof Mono<?> monoResult) {
					monoConvertAndSend(monoResult, kafkaKey);
				}
			} catch (IllegalAccessException | InvocationTargetException | JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		});
		container.start();
		containers.add(container);
	}

	@PostConstruct
	public void startListeningToRequests() throws SecurityException {
		applicationContext.getBeansWithAnnotation(KafkaMessageEnable.class).values().forEach(bean -> {
			Object service = kafkaProxyCreator.createGenericService(bean.getClass());
			for (Method method : bean.getClass().getMethods()) {
				if (method.isAnnotationPresent(KafkaMessageListener.class)) {
					registerListener(method, service);
				}
			}
		});
	}

	@PreDestroy
	public void stopContainers() {
		for (ConcurrentMessageListenerContainer<String, String> container : containers) {
			if (container != null) {
				container.stop();
			}
		}
		containers.clear();
	}
}
