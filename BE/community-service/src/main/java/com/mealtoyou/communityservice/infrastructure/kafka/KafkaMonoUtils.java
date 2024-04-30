package com.mealtoyou.communityservice.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaMonoUtils {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaResponseListener responseListener;
    private final ObjectMapper objectMapper;

    public Mono<String> sendAndReceive(String topic, Object message) {
        String requestId = UUID.randomUUID().toString();
        try {
            if (message instanceof String stringMessage)
                kafkaTemplate.send(topic, requestId, stringMessage);
            else
                kafkaTemplate.send(topic, requestId, objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return responseListener.registerResponseHandler(requestId);
    }
}
