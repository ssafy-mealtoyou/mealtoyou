package com.mealtoyou.userservice.application.service;

import com.mealtoyou.userservice.application.dto.request.UserInfoRequestDto;
import com.mealtoyou.userservice.application.dto.response.HealthInfoResponseDto;
import com.mealtoyou.userservice.application.dto.response.UserInfoResponseDto;
import com.mealtoyou.userservice.domain.model.Intermittent;
import com.mealtoyou.userservice.domain.model.User;
import com.mealtoyou.userservice.domain.repository.IntermittentRepository;
import com.mealtoyou.userservice.domain.repository.UserRepository;
import com.mealtoyou.userservice.infrastructure.kafka.KafkaMonoUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final IntermittentRepository intermittentRepository;
	private final S3Uploader s3Uploader;
	private final KafkaMonoUtils kafkaMonoUtils;

	public Mono<UserInfoResponseDto> getUserProfile(long userId) {
		return userRepository.findById(userId).flatMap(user -> {
			if (user == null) {
				return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
			}
			return Mono.just(UserInfoResponseDto.fromEntity(user));
		});
	}

	public Mono<UserInfoResponseDto> updateUserProfile(long userId, FilePart image,
			UserInfoRequestDto userInfoRequestDto) {
		Mono<String> imageUrl;
		if(image!=null){
			imageUrl = s3Uploader.upload(image);
		} else {
			imageUrl = userRepository.findById(userId).map(User::getUserImageUrl);
    }
    return imageUrl.flatMap(url ->
				userRepository.findById(userId)
						.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
						.flatMap(user -> {
							user.updateUserInfo(userInfoRequestDto, url);
							return userRepository.save(user);
						})
						.map(UserInfoResponseDto::fromEntity));
	}

	public Mono<Double> getHeight(Long userId) {
		return userRepository
			.findById(userId).map(User::getHeight);
	}

	public Mono<String> getNickname(Long userId) {
		return userRepository
			.findById(userId).map(User::getNickname);
	}


	private Mono<Double> requestCurrentYearWeight(long userId) {
		return kafkaMonoUtils.sendAndReceive("health-service-getCurrentYearWeight", userId).map(Double::parseDouble);
	}
	private Mono<Double> requestLastMonthWeight(long userId) {
		return kafkaMonoUtils.sendAndReceive("health-service-getLastMonthWeight", userId).map(Double::parseDouble);
	}

	private Mono<Double> requestMuscle(long userId) {
		return kafkaMonoUtils.sendAndReceive("health-service-getMuscle", userId).map(Double::parseDouble);
	}

	private Mono<Double> requestFat(long userId) {
		return kafkaMonoUtils.sendAndReceive("health-service-getFat", userId).map(Double::parseDouble);
	}

	public Mono<HealthInfoResponseDto> getHealthInfo(long userId) {
		Mono<User> monoUser = userRepository.findById(userId).switchIfEmpty(Mono.just(User.builder().build()));
		Mono<Intermittent> monoIntermittent = intermittentRepository.findByUserId(userId).switchIfEmpty(Mono.just(Intermittent.builder().build()));
		Mono<Double> monoCurrentYearWeight = requestCurrentYearWeight(userId);
		Mono<Double> monoLastMonthWeight = requestLastMonthWeight(userId);
		Mono<Double> monoMuscle = requestMuscle(userId);
		Mono<Double> monoFat = requestFat(userId);
		return Mono.zip(
				monoUser,
				monoIntermittent,
				monoCurrentYearWeight,
				monoLastMonthWeight,
				monoMuscle,
				monoFat
		).map(tuple -> {
			User user = tuple.getT1();
			Intermittent intermittent = tuple.getT2();
			double currentYearWeight = tuple.getT3();
			double lastMonthWeight = tuple.getT4();
			double muscle = tuple.getT5();
			double fat = tuple.getT6();
			// DTO 생성
			return HealthInfoResponseDto.builder()
					.nickname(user.getNickname())
					.imageUrl(user.getUserImageUrl())
					.inbodyBoneMuscle(muscle)
					.inbodyBodyFat(fat)
					.intermittentYn(user.isIntermittentFasting())
					.intermittentStartTime(intermittent.getStartTime())
					.intermittentEndTime(intermittent.getEndTime())
					.weight(user.getWeight())
					.weightLastMonth(lastMonthWeight)
					.weightThisYear(currentYearWeight)
					.goalWeight(Optional.ofNullable(user.getGoalWeight()).orElse(0.0))
					.goalDate(user.getGoalEndDate())
					.build();
		});
	}
}
