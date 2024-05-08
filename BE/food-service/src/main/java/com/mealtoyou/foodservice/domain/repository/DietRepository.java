package com.mealtoyou.foodservice.domain.repository;

import java.time.LocalDateTime;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mealtoyou.foodservice.domain.model.Diet;

import reactor.core.publisher.Flux;

public interface DietRepository extends ReactiveCrudRepository<Diet, Long> {
	Flux<Diet> findAllByUserIdAndCreateDateTimeBetween(Long userId, LocalDateTime startTime,
		LocalDateTime endTime);
}
