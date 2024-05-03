package com.mealtoyou.healthservice.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.mealtoyou.healthservice.domain.model.Exercise;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ExerciseRepository extends ReactiveCrudRepository<Exercise, Long> {
	Flux<Exercise> findAllByUserId(Long userId);

	@Query("SELECT * FROM exercise WHERE user_id = :userId AND step_start_date = :stepStartDate")
	Flux<Exercise> findByUserIdAndStepStartDate(Long userId, LocalDate stepStartDate);
	@Query("INSERT INTO exercise (user_id, steps, calories_burned, step_start_date, calories_start_date) VALUES (:userId, :steps, :caloriesBurned, :stepStartDate, :caloriesStartDate) ON DUPLICATE KEY UPDATE steps = VALUES(steps), calories_burned = VALUES(calories_burned)")
	Mono<String> upsertExercise(Long userId, Long steps, Double caloriesBurned, LocalDate stepStartDate, LocalDate caloriesStartDate);
}

