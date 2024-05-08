package com.mealtoyou.communityservice.domain.repository;

import com.mealtoyou.communityservice.domain.model.UserCommunity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserCommunityRepository extends ReactiveCrudRepository<UserCommunity, Long> {
    Mono<UserCommunity> findByUserId(Long userId);
}
