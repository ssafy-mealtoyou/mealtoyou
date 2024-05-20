package com.mealtoyou.healthservice.domain.repository;

import java.time.LocalDate;
import java.util.List;

import com.netflix.appinfo.ApplicationInfoManager;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.mealtoyou.healthservice.domain.model.Exercise;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ExerciseRepository extends ReactiveCrudRepository<Exercise, Long> {

	@Query("SELECT * FROM exercise WHERE user_id=:userId ORDER BY step_start_date limit :day")
	Flux<Exercise> findAllByUserId(Long userId, Integer day);

	@Query("SELECT * FROM exercise WHERE user_id = :userId AND step_start_date = :stepStartDate")
	Flux<Exercise> findByUserIdAndStepStartDate(Long userId, LocalDate stepStartDate);

	Mono<Exercise> findFirstByUserIdAndStepStartDateOrderByStepStartDateDesc(long l, LocalDate now);
}

