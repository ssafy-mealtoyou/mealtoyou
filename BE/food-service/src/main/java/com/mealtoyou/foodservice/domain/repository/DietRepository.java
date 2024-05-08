package com.mealtoyou.foodservice.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mealtoyou.foodservice.domain.model.Diet;

public interface DietRepository extends ReactiveCrudRepository<Diet, Long> {
}
