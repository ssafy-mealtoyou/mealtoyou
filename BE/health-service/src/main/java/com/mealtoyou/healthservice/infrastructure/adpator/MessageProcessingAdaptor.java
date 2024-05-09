package com.mealtoyou.healthservice.infrastructure.adpator;

import com.mealtoyou.healthservice.application.dto.UserHealthInfo;
import com.mealtoyou.healthservice.application.service.BodyService;
import com.mealtoyou.healthservice.domain.repository.ExerciseRepository;
import com.mealtoyou.healthservice.infrastructure.kafka.KafkaMessageEnable;
import com.mealtoyou.healthservice.infrastructure.kafka.KafkaMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@KafkaMessageEnable
@RequiredArgsConstructor
public class MessageProcessingAdaptor {

    private final ExerciseRepository exerciseRepository;
    private final BodyService bodyService;

    @KafkaMessageListener(topic = "getBmr")
    public Mono<Double> getBmr(String message) {
        return bodyService.getUserBmr(Long.parseLong(message));
    }

    @KafkaMessageListener(topic = "healthInfo")
    public Mono<UserHealthInfo> processMessage1(String message) {
        log.info("start");
        return exerciseRepository.findFirstByUserIdOrderByStepStartDateDesc(Long.parseLong(message))
                .map(result ->
                        new UserHealthInfo(
                                result.getSteps().intValue(),
                                result.getCaloriesBurned().intValue())
                );

    }

    @KafkaMessageListener(topic = "requests2")
    public String processMessage2(String message) {
        return message + "2번 MSA입니다.";
    }

}
