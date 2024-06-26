package com.mealtoyou.healthservice.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class KafkaResponseListener {
    private final Map<String, Sinks.One<String>> pendingResponses = new ConcurrentHashMap<>();

    @KafkaListener(topics = "#{kafkaTopicUtils.getResponseTopic()}")
    public void onResponse(ConsumerRecord<String, String> record) {
        Sinks.One<String> sink = pendingResponses.remove(record.key());
        if (sink != null) {
            sink.tryEmitValue(record.value());
        }
    }

    public Mono<String> registerResponseHandler(String requestId) {
        Sinks.One<String> sink = Sinks.one();
        pendingResponses.put(requestId, sink);
        return sink.asMono();
    }
}
