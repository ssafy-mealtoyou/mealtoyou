package com.mealtoyou.communityservice.domain.repository;

import com.mealtoyou.communityservice.domain.model.UserCommunity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserCommunityRepository extends ReactiveCrudRepository<UserCommunity, Long> {
}
