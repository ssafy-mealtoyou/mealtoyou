package com.mealtoyou.communityservice.domain.repository;

import com.mealtoyou.communityservice.domain.model.Community;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CommunityRepository extends ReactiveCrudRepository<Community, Long> {

    Mono<Community> findByLeaderId(Long userId);
}
