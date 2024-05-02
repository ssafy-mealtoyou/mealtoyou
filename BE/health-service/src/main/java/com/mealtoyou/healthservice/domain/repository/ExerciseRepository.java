package com.mealtoyou.healthservice.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.mealtoyou.healthservice.domain.model.Exercise;

@Repository
public interface ExerciseRepository extends ReactiveCrudRepository<Exercise, Long> {

}

