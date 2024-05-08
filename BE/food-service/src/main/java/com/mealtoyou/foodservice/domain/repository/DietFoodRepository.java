package com.mealtoyou.foodservice.domain.repository;

import java.util.List;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mealtoyou.foodservice.domain.model.DietFood;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DietFoodRepository  extends ReactiveCrudRepository<DietFood, Long> {
	Flux<DietFood> findDietFoodsByDietId(long dietId);
	Flux<DietFood> findDietFoodsByDietIdIn(List<Long> dietIds);
}
