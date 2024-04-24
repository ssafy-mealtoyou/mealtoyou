package com.mealtoyou.userservice.domain.repository;

import com.mealtoyou.userservice.domain.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
