package com.mealtoyou.healthservice.application.service;

import com.mealtoyou.healthservice.application.dto.BodyDto;
import com.mealtoyou.healthservice.domain.model.Body;
import com.mealtoyou.healthservice.domain.repository.BodyRepository;
import com.mealtoyou.healthservice.infrastructure.kafka.KafkaMonoUtils;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

	public Mono<Double> getUserBmr(Long userId) {
		// 가장 최근의 BMR 기록 1개 불러오기
		return bodyRepository.findByUserId(userId, 1)
			.collectList()
			.map(bodyList -> bodyList.get(0).getBmr());
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
	public Mono<Double> getCurrentYearWeightInfo(Long userId){
		// 올해 데이터 가져오기
		int currentYear = LocalDate.now().getYear();
		return bodyRepository.findByUserIdAndCurrentYear(userId, currentYear).switchIfEmpty(Mono.just(0.0));
	}
	public Mono<Double> getLastMonthWeightInfo(Long userId) {
		LocalDate startOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
		LocalDate endOfLastMonth = startOfLastMonth.withDayOfMonth(startOfLastMonth.lengthOfMonth());

		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		String startDate = startOfLastMonth.format(formatter);
		String endDate = endOfLastMonth.format(formatter);
		// 평균 몸무게 요청
		return bodyRepository.findByUserIdAndStartOfLastMonthAndEndOfLastMonth(userId, startDate, endDate).switchIfEmpty(Mono.just(0.0));

	}

	public Mono<Double> getUserMuscle(Long userId) {
		return bodyRepository.findByUserId(userId, 1)
				.collectList()
				.flatMap(bodyList -> {
					if (bodyList.isEmpty()) {
						return Mono.just(0.0); // 기본값 0.0 반환
					}
					return Mono.just(bodyList.get(0).getSkeletalMuscle());
				});
	}

	public Mono<Double> getUserFat(Long userId) {
		return bodyRepository.findByUserId(userId, 1)
				.collectList()
				.flatMap(bodyList -> {
					if (bodyList.isEmpty()) {
						return Mono.just(0.0); // 기본값 0.0 반환
					}
					return Mono.just(bodyList.get(0).getBodyFat());
				});
	}
}
