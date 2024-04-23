package com.mealtoyou.sample.domain.repository;

import com.mealtoyou.sample.domain.model.Person;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PersonRepository extends ReactiveCrudRepository<Person, Long> {
}