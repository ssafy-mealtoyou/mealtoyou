package com.mealtoyou.healthservice.application.service;

import java.text.DecimalFormat;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.mealtoyou.healthservice.application.dto.BodyDto;
import com.mealtoyou.healthservice.domain.model.Body;
import com.mealtoyou.healthservice.domain.repository.BodyRepository;
import com.mealtoyou.healthservice.infrastructure.kafka.KafkaMonoUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BodyService {
	private final JwtTokenProvider jwtTokenProvider;
	private final BodyRepository bodyRepository;
	private final KafkaMonoUtils kafkaMonoUtils;
	public Mono<String> saveBodyData(String token, BodyDto bodyDto) {
		DecimalFormat df = new DecimalFormat("#.0");
		Long userId = jwtTokenProvider.getUserId(token);
		LocalDate measuredDate = bodyDto.getMeasuredDate();
		return kafkaMonoUtils.sendAndReceive("user-service-getHeight",userId)
			.flatMap(height -> {

				Double formattedBodyFat = Double.valueOf(df.format(bodyDto.getBodyFat()));
				Double formattedWeight = Double.valueOf(df.format(bodyDto.getWeight()));
				Double formattedSkeletalMuscle = Double.valueOf(df.format(bodyDto.getSkeletalMuscle()));
				Double formattedBmr = Double.valueOf(df.format(bodyDto.getBmr()));
				Double bmi = formattedWeight / Math.pow(Double.parseDouble(height),2);

				return bodyRepository.findByUserIdandMeasuredDate(userId,measuredDate)
					.flatMap(existingBody -> {
						Body updatedBody = existingBody.toBuilder()
							.userId(userId)
							.bodyFat(formattedBodyFat)
							.weight(formattedWeight)
							.skeletalMuscle(formattedSkeletalMuscle)
							.measuredDate(bodyDto.getMeasuredDate())
							.bmr(formattedBmr)
							.bmi(bmi)
							.build();
						return bodyRepository.save(updatedBody);
					})
					.switchIfEmpty(Mono.defer(() -> {
						Body newBody = Body.builder()
							.userId(userId)
							.bodyFat(formattedBodyFat)
							.measuredDate(bodyDto.getMeasuredDate())
							.weight(formattedWeight)
							.skeletalMuscle(formattedSkeletalMuscle)
							.bmr(formattedBmr)
							.bmi(bmi)
							.build();
						return bodyRepository.save(newBody);
					}))
					.then(Mono.just("Exercise data saved successfully")) // 모든 작업이 성공적으로 완료됨
					.onErrorResume(e -> Mono.error(new RuntimeException("Error saving body data", e))); // 에러 처리
			});

	}

	public Flux<BodyDto> readBodyData(String token, Integer day) {
		Long userId = jwtTokenProvider.getUserId(token);
		Flux<Body> bodyFlux = bodyRepository.findByUserId(userId,day);
		return bodyFlux.map(
			body -> BodyDto.builder()
				.bodyFat(body.getBodyFat())
				.weight(body.getWeight())
				.bmi(body.getBmi())
				.bmr(body.getBmr())
				.skeletalMuscle(body.getSkeletalMuscle())
				.measuredDate(body.getMeasuredDate())
				.build()
		);
	}
}
