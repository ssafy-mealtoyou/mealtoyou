package com.mealtoyou.foodservice.domain.repository;

import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;

import com.mealtoyou.foodservice.domain.model.Food;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FoodRepository extends ReactiveElasticsearchRepository<Food, String> {
	Flux<Food> findByName(String name);
	Mono<Food> findByRid(Long rid);
}
