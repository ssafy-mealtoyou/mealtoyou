package com.mealtoyou.supplementservice.infrastructure.kafka;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KafkaMonoUtils {
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final KafkaResponseListener responseListener;
	private final ObjectMapper objectMapper;
	private final KafkaTopicUtils kafkaTopicUtils;

	public Mono<String> sendAndReceive(String topic, Object message) {
		String requestId = UUID.randomUUID().toString();
		try {
			KafkaKey kafkaKey = new KafkaKey(requestId, kafkaTopicUtils.getResponseTopic());
			String key = objectMapper.writeValueAsString(kafkaKey);
			if (message instanceof String stringMessage) {
				kafkaTemplate.send(topic, key, stringMessage);
			} else
				kafkaTemplate.send(topic, key, objectMapper.writeValueAsString(message));
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return responseListener.registerResponseHandler(requestId);
	}
}
