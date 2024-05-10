package com.mealtoyou.userservice.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mealtoyou.userservice.domain.model.UserInbodyLog;

public interface UserInbodyLogRepository extends ReactiveCrudRepository<UserInbodyLog, Long> {
}
