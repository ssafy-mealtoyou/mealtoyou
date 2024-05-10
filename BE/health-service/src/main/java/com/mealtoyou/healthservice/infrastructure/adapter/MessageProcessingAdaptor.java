package com.mealtoyou.healthservice.infrastructure.adapter;

import com.mealtoyou.healthservice.application.service.BodyService;
import com.mealtoyou.healthservice.infrastructure.kafka.KafkaMessageEnable;
import com.mealtoyou.healthservice.infrastructure.kafka.KafkaMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@KafkaMessageEnable
@RequiredArgsConstructor
@Slf4j
public class MessageProcessingAdaptor {

    private final BodyService bodyService;

    @KafkaMessageListener(topic = "getBmr")
    public Mono<Double> getBmr(String message) {
        return bodyService.getUserBmr(Long.parseLong(message));
    }

    @KafkaMessageListener(topic = "getCurrentYearWeight")
    public Mono<Double> getCurrentYearWeight(String message) {
        return bodyService.getCurrentYearWeightInfo(Long.parseLong(message));
    }

    @KafkaMessageListener(topic = "getLastMonthWeight")
    public Mono<Double> getLastMonthWeight(String message) {
        return bodyService.getLastMonthWeightInfo(Long.parseLong(message));
    }

    @KafkaMessageListener(topic = "getMuscle")
    public Mono<Double> getMuscle(String message) {
        return bodyService.getUserMuscle(Long.parseLong(message));
    }
    @KafkaMessageListener(topic = "getFat")
    public Mono<Double> getFat(String message) {
        return bodyService.getUserFat(Long.parseLong(message));
    }

    @KafkaMessageListener(topic = "test")
    public String processMessage2(String message) {
        return "Test Message";
    }

}
