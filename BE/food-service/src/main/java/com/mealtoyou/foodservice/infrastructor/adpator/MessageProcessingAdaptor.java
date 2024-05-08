package com.mealtoyou.foodservice.infrastructor.adpator;

import org.springframework.stereotype.Service;

import com.mealtoyou.foodservice.infrastructor.kafka.KafkaMessageEnable;
import com.mealtoyou.foodservice.infrastructor.kafka.KafkaMessageListener;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@KafkaMessageEnable
@RequiredArgsConstructor
public class MessageProcessingAdaptor {

    @KafkaMessageListener(topic = "requests1")
    public String processMessage1(String message) {
        return message + "1번 MSA입니다.";
    }

    @KafkaMessageListener(topic = "requests2")
    public String processMessage2(String message) {
        return message + "2번 MSA입니다.";
    }

}
