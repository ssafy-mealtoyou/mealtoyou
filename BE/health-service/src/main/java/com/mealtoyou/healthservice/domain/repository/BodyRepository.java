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

	@Query("SELECT * FROM user_health WHERE user_id=:userId ORDER BY measured_date limit :day")
	Flux<Body> findByUserId(Long userId, int day);

	// 지난달 평균 몸무게 가져오는 쿼리
	@Query("SELECT AVG(weight) FROM user_health WHERE user_id = :userId AND measured_date BETWEEN :startOfLastMonth AND :endOfLastMonth")
	Mono<Double> findByUserIdAndStartOfLastMonthAndEndOfLastMonth(Long userId, String startOfLastMonth, String endOfLastMonth);

	// 올해 평균 몸무게 가져오는 쿼리
	@Query("SELECT AVG(weight) FROM user_health WHERE user_id = :userId AND YEAR(measured_date) = :currentYear")
	Mono<Double> findByUserIdAndCurrentYear(Long userId, int currentYear);
}
