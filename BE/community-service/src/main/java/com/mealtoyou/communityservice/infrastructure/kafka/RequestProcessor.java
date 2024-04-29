package com.mealtoyou.communityservice.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

    private void fluxConvertAndSend(Flux<?> fluxResult, String key) {
        fluxResult.collectList().subscribe(
                item -> {
                    try {
                        kafkaTemplate.send(kafkaTopicUtils.getResponseTopic(), key, objectMapper.writeValueAsString(item));
                    } catch (JsonProcessingException e) {
                        log.error("JSON formatting error for key: {} with item: {}", key, item, e);
                    }
                },
                error -> log.error("Error processing the Mono for key: {}", key, error));
    }

    private void monoConvertAndSend(Mono<?> monoResult, String key) {
        monoResult.subscribe(
                item -> {
                    try {
                        String message = objectMapper.writeValueAsString(item);
                        kafkaTemplate.send(kafkaTopicUtils.getResponseTopic(), key, message);
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
        ConcurrentMessageListenerContainer<String, String> container = factory.createContainer(kafkaTopicUtils.getRequestTopic(config.topic()));

        container.setupMessageListener((MessageListener<String, String>) record -> {
            String message = record.value();
            String key = record.key();
            try {
                Object result = method.invoke(service, message);
                if (result instanceof String stringResult) {
                    kafkaTemplate.send(kafkaTopicUtils.getResponseTopic(), key, stringResult);
                } else if (result instanceof Flux<?> fluxResult) {
                    fluxConvertAndSend(fluxResult, key);
                } else if (result instanceof Mono<?> monoResult) {
                    monoConvertAndSend(monoResult, key);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
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
