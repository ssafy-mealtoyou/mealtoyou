package com.mealtoyou.userservice.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.mealtoyou.userservice.domain.model.Intermittent;

import reactor.core.publisher.Mono;

@Repository
public interface IntermittentRepository extends ReactiveCrudRepository<Intermittent, Long> {
	Mono<Intermittent> findByUserId(Long userId);
}
