package com.mealtoyou.userservice.application.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.userservice.application.dto.DailyDietFoodRequestDto;
import com.mealtoyou.userservice.application.dto.DailyDietsResponseDto;
import com.mealtoyou.userservice.application.dto.DailyExerciseRequestDto;
import com.mealtoyou.userservice.application.dto.ExerciseDto;
import com.mealtoyou.userservice.application.dto.request.UserGoalRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserInbodyRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserInfoRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserWeightRequestDto;
import com.mealtoyou.userservice.application.dto.response.HealthInfoResponseDto;
import com.mealtoyou.userservice.application.dto.response.UserHomeResponseDto;
import com.mealtoyou.userservice.application.dto.response.UserInfoResponseDto;
import com.mealtoyou.userservice.domain.model.Intermittent;
import com.mealtoyou.userservice.domain.model.User;
import com.mealtoyou.userservice.domain.repository.IntermittentRepository;
import com.mealtoyou.userservice.domain.repository.UserRepository;
import com.mealtoyou.userservice.infrastructure.kafka.KafkaMonoUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;
	private final IntermittentRepository intermittentRepository;
	private final S3Uploader s3Uploader;
	private final KafkaMonoUtils kafkaMonoUtils;

	private Mono<Boolean> requestSavingUserHealth(UserInbodyRequestDto requestDto) {
		return kafkaMonoUtils.sendAndReceive("health-service-save-user-inbody", requestDto)
			.map(Boolean::parseBoolean)
			.onErrorResume((e) -> Mono.just(false));
	}

	private Mono<Double> requestBMR(long userId) {
		return kafkaMonoUtils.sendAndReceive("health-service-getBmr", userId).map(Double::parseDouble);
	}

	private Mono<DailyDietsResponseDto> requestTodayDietsInfo(long userId) {
		DailyDietFoodRequestDto dailyDietFoodRequestDto = DailyDietFoodRequestDto.builder()
			.userId(userId)
			.date(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
			.build();
		return kafkaMonoUtils.sendAndReceive("food-service-getFoodListByDate", dailyDietFoodRequestDto)
			.map(message -> {
				try {
					return objectMapper.readValue(message, DailyDietsResponseDto.class);
				} catch (JsonProcessingException e) {
					return DailyDietsResponseDto.builder().build();
				}
			});
	}

	private Mono<ExerciseDto> requestTodayUserExercise(long userId) {
		DailyExerciseRequestDto dto = DailyExerciseRequestDto.builder()
			.userId(userId)
			.date(LocalDate.now())
			.build();
		return kafkaMonoUtils.sendAndReceive("health-service-getExerciseByDate", dto)
			.map(message -> {
				try {
					return objectMapper.readValue(message, ExerciseDto.class);
				} catch (JsonProcessingException e) {
					return ExerciseDto.builder().build();
				}
			});
	}

	private int calcNutrientsPer(double bmr, double ratio, double nutrientsGram, int caloriesFactor) {
		return (int)(((nutrientsGram * caloriesFactor) / (bmr * ratio)) * 100);
	}

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
		if (image != null) {
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
		Mono<Intermittent> monoIntermittent = intermittentRepository.findByUserId(userId)
			.switchIfEmpty(Mono.just(Intermittent.builder().build()));
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
				.goalWeight(Optional.ofNullable(user.getGoalWeight()).orElse(0))
				.goalDate(user.getGoalEndDate())
				.build();
		});
	}

	public Mono<Void> updateGoal(long userId, UserGoalRequestDto requestDto) {
		// return userRepository.findById(userId)
		// 	.flatMap(user -> {
		// 		user.updateGoal(requestDto);
		// 		return userRepository.save(user).then();
		// 	});
		return userRepository.findById(userId)
			.flatMap(user -> {
				user.updateGoal(requestDto.goalWeight(), requestDto.getParsedGoalEndDate());
				return userRepository.save(user).then();
			});
	}

	public Mono<Void> updateWeight(long userId, UserWeightRequestDto requestDto) {
		return userRepository.findById(userId).flatMap(user -> {
			user.updateWeight(requestDto.weight());
			return userRepository.save(user);
		}).then();
	}

	public Mono<Void> updateInbody(long userId, String token, UserInbodyRequestDto requestDto) {
		return userRepository.findById(userId).flatMap(user -> {
				if (user.getHeight() <= 0.0 || user.getWeight() <= 0.0) {
					return Mono.error(new RuntimeException("유효하지 않은 체중 또는 키 값입니다."));
				}
				requestDto.setOthers(token, user.getWeight(), user.getHeight(), user.getAge());
				return requestSavingUserHealth(requestDto);
			})
			.flatMap(res -> {
				if (!res)
					return Mono.error(new RuntimeException());
				return Mono.empty();
			});
	}

	// public Mono<UserHomeResponseDto> getUserHome(long userId) {
	public Mono<UserHomeResponseDto> getUserHome(long userId) {
		Mono<DailyDietsResponseDto> dailyDietsResponseDtoMono = requestTodayDietsInfo(userId);
		Mono<Double> bmrMono = requestBMR(userId);
		Mono<ExerciseDto> exerciseDtoMono = requestTodayUserExercise(userId);
		Mono<User> userMono = userRepository.findById(userId);

		return Mono.zip(dailyDietsResponseDtoMono, bmrMono, exerciseDtoMono, userMono)
			.map(tuple -> {
				DailyDietsResponseDto dietsResponseDto = tuple.getT1();
				double bmr = tuple.getT2();
				ExerciseDto exerciseDto = tuple.getT3();
				User user = tuple.getT4();

				Long steps = exerciseDto.getSteps();
				if (steps == null)
					steps = 0L;

				int activityPer = Math.min(100, (int)((steps / 8000) * 100));
				int dietPer = Math.min(100, (int)((dietsResponseDto.diets().size() / 3.0) * 100.0));
				int caloriesPer = Math.min(100, calcNutrientsPer(bmr, 1.0, dietsResponseDto.dailyCaloriesBurned(), 1));
				// int carbohydratePer = calcNutrientsPer(bmr, 0.5, dietsResponseDto.dailyCarbohydrateTaked(), 4);
				// int proteinPer = calcNutrientsPer(bmr, 0.25, dietsResponseDto.dailyCarbohydrateTaked(), 4);
				// int fatPer = calcNutrientsPer(bmr, 0.25, dietsResponseDto.dailyCarbohydrateTaked(), 9);

				return UserHomeResponseDto.builder()
					.daySummary(UserHomeResponseDto.DaySummary.builder()
						.activityPer(activityPer)
						.dietPer(dietPer)
						.caloriesPer(caloriesPer)
						.build())
					.goalWeight(user.getGoalWeight())
					.goalEndDate(user.getGoalEndDate())
					.goalStartDate(user.getGoalStartDate())
					.goalStartWeight(user.getGoalStartWeight())
					.currentWeight(user.getWeight())
					.build();
			});
	}
}