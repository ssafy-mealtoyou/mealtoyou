package com.mealtoyou.communityservice.domain.repository;

import com.mealtoyou.communityservice.domain.model.CommunityDiet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CommunityDietRepository extends ReactiveCrudRepository<CommunityDiet, Long> {
    Flux<Long> findByCommunityId(Long communityId);
}
