package com.mealtoyou.foodservice.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mealtoyou.foodservice.domain.model.DietFood;

public interface DietFoodRepository  extends ReactiveCrudRepository<DietFood, Long> {
}
