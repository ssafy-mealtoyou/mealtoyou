package com.mealtoyou.communityservice.domain.repository;

import com.mealtoyou.communityservice.domain.model.Community;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends ReactiveCrudRepository<Community, Long> {

}
