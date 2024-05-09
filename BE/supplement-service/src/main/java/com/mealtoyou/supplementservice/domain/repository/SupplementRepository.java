package com.mealtoyou.supplementservice.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.mealtoyou.supplementservice.domain.model.Supplement;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SupplementRepository extends ReactiveCrudRepository<Supplement, Long> {
	Mono<Supplement> findByUserIdAndName(Long userId, String name);
	Flux<Supplement> findByUserId(Long userId);
}
