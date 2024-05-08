package com.mealtoyou.chattingservice.domain.repository;

import com.mealtoyou.chattingservice.domain.model.Chat;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, ObjectId> {

    @Query("{ 'groupId': ?0, '_id': { $lt: ?1 } }")
    Flux<Chat> findByGroupIdAndIdLessThanOrderByTimestampDesc(Long groupId, ObjectId lastId, Pageable pageable);

    Flux<Chat> findByGroupIdOrderByTimestampDesc(Long groupId);

    @Query("{ 'groupId': ?0 }")
    Flux<Chat> findTop3ByGroupIdOrderByTimestampDesc(Long groupId, Pageable pageable);

}
