package com.mealtoyou.foodservice.domain.repository;

import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;

import com.mealtoyou.foodservice.domain.model.Food;

import reactor.core.publisher.Flux;

public interface FoodRepository extends ReactiveElasticsearchRepository<Food, String> {
	Flux<Food> findByName(String name);
}
