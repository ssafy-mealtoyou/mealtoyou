package com.mealtoyou.healthservice.domain.repository;

import java.time.LocalDate;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.mealtoyou.healthservice.domain.model.Body;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BodyRepository extends ReactiveCrudRepository<Body, Long> {

	@Query("SELECT * FROM user_health WHERE user_id=:userId and measured_date=:measuredDate")
	Flux<Body> findByUserIdandMeasuredDate(Long userId, LocalDate measuredDate);

	@Query("SELECT * FROM user_health WHERE user_id=:userId ORDER BY measured_date DESC limit :day")
	Flux<Body> findByUserId(Long userId, int day);
}
