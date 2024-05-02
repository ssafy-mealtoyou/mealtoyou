package com.mealtoyou.healthservice.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

import org.springframework.stereotype.Service;

import com.mealtoyou.healthservice.application.dto.ExerciseRequestDto;
import com.mealtoyou.healthservice.domain.model.Exercise;
import com.mealtoyou.healthservice.domain.repository.ExerciseRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExerciseService {
	private final ExerciseRepository exerciseRepository;
	private final JwtTokenProvider jwtTokenProvider;
	public Mono<String> saveExerciseData(String token, ExerciseRequestDto exerciseRequestDto) {
		Long userId = jwtTokenProvider.getUserId(token);
		System.out.println(userId);
		try {

			Exercise exercise = Exercise.builder()
				.userId(userId)
				.steps(exerciseRequestDto.getSteps())
				.caloriesBurned(exerciseRequestDto.getCaloriesBurned())
				.stepStartDate((LocalDate)exerciseRequestDto.getStepStartDate())
				// .stepEndDate((LocalDate)exerciseRequestDto.getStepEndDate())
				.caloriesStartDate((LocalDate)exerciseRequestDto.getCaloriesStartDate())
				// .caloriesEndDate((LocalDate)exerciseRequestDto.getCaloriesEndDate())
				.build();
			System.out.println(exercise.getSteps() + " " + exercise.getCaloriesBurned() + " " + exercise.getStepStartDate());
			return exerciseRepository.save(exercise)
				.thenReturn("Exercise data saved successfully");
		} catch (Exception e) {
			return Mono.error(new RuntimeException("Error saving exercise data", e));
		}
	}

	private LocalDateTime convertToDateTime(Temporal temporal) {
		if (temporal instanceof LocalDateTime) {
			return (LocalDateTime) temporal;
		} else {
			throw new IllegalArgumentException("Temporal value is not a LocalDateTime");
		}
	}

	private Double extractDouble(Comparable<?> comparable) {
		if (comparable instanceof Double) {
			return (Double) comparable;
		} else if (comparable instanceof Number) {
			return ((Number) comparable).doubleValue();
		} else {
			throw new IllegalArgumentException("Calorie value is not a number");
		}
	}
}
