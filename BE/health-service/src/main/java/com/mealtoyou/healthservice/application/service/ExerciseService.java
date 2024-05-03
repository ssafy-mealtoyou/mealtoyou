package com.mealtoyou.healthservice.application.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.mealtoyou.healthservice.application.dto.ExerciseDto;
import com.mealtoyou.healthservice.domain.model.Exercise;
import com.mealtoyou.healthservice.domain.repository.ExerciseRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExerciseService {
	private final ExerciseRepository exerciseRepository;
	private final JwtTokenProvider jwtTokenProvider;
	public Mono<String> saveExerciseData(String token, ExerciseDto exerciseDto) {
		Long userId = jwtTokenProvider.getUserId(token); // 토큰에서 사용자 ID 추출
		LocalDate stepStartDate = exerciseDto.getStepStartDate(); // DTO에서 시작 날짜 추출

		// 사용자 ID와 시작 날짜로 기존 데이터 조회
		return exerciseRepository.findByUserIdAndStepStartDate(userId, stepStartDate)
			.flatMap(existingExercise -> {
				// 데이터가 있으면 업데이트
				Exercise updatedExercise = existingExercise.toBuilder()
					.steps(exerciseDto.getSteps())
					.caloriesBurned(exerciseDto.getCaloriesBurned())
					.caloriesStartDate(exerciseDto.getCaloriesStartDate())
					.build();
				return exerciseRepository.save(updatedExercise); // 업데이트된 데이터 저장
			})
			.switchIfEmpty(Mono.defer(() -> {
				// 데이터가 없으면 새로 생성
				Exercise newExercise = Exercise.builder()
					.userId(userId)
					.steps(exerciseDto.getSteps())
					.caloriesBurned(exerciseDto.getCaloriesBurned())
					.stepStartDate(stepStartDate)
					.caloriesStartDate(exerciseDto.getCaloriesStartDate())
					.build();
				return exerciseRepository.save(newExercise); // 새 데이터 저장
			}))
			.then(Mono.just("Exercise data saved successfully")) // 모든 작업이 성공적으로 완료됨
			.onErrorResume(e -> Mono.error(new RuntimeException("Error saving exercise data", e))); // 에러 처리
	}

	public Flux<ExerciseDto> readExerciseData(String token, Integer day) {
		Long userId = jwtTokenProvider.getUserId(token);
		Flux<Exercise> exerciseFlux = exerciseRepository.findAllByUserId(userId,day);
		return exerciseFlux
			.map(exercise -> ExerciseDto.builder()
				.steps(exercise.getSteps())
				.caloriesBurned(exercise.getCaloriesBurned())
				.stepStartDate((LocalDate)exercise.getStepStartDate())
				.caloriesStartDate((LocalDate)exercise.getCaloriesStartDate())
				.build());
	}

}
