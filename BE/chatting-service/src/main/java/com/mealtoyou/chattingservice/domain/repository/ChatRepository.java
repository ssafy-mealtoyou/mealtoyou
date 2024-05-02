package com.mealtoyou.chattingservice.domain.repository;

import com.mealtoyou.chattingservice.domain.model.Chat;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

}
