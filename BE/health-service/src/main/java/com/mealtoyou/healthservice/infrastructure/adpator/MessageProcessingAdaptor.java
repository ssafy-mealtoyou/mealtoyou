package com.mealtoyou.healthservice.infrastructure.adpator;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.healthservice.application.dto.UserHealthInfo;
import com.mealtoyou.healthservice.application.dto.UserInbodyRequestDto;
import com.mealtoyou.healthservice.application.service.BodyService;
import com.mealtoyou.healthservice.domain.repository.ExerciseRepository;
import com.mealtoyou.healthservice.infrastructure.kafka.KafkaMessageEnable;
import com.mealtoyou.healthservice.infrastructure.kafka.KafkaMessageListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@KafkaMessageEnable
@RequiredArgsConstructor
public class MessageProcessingAdaptor {

    private final ObjectMapper objectMapper;
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

    @KafkaMessageListener(topic = "save-user-inbody")
    public Mono<Boolean> saveUserInbody(String message) {
        log.info("start save-user-inbody");
		UserInbodyRequestDto userInbodyRequestDto;
		try {
			userInbodyRequestDto = objectMapper.readValue(message, UserInbodyRequestDto.class);
		} catch (JsonProcessingException e) {
            log.error("요청 메세지 매핑 실패", e);
			return Mono.just(false);
		}
		return bodyService.saveBodyData(userInbodyRequestDto)
			.map(s -> true)
			.onErrorResume(e -> {
				log.error("저장중 에러 발생", e);
				return Mono.just(false);
			});
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

}
