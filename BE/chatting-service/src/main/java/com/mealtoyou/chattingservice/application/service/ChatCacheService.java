package com.mealtoyou.chattingservice.application.service;

import com.mealtoyou.chattingservice.domain.model.Chat;
import com.mealtoyou.chattingservice.domain.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatCacheService {
    private final ChatRepository chatRepository;

    public Mono<List<Chat>> getRecentChatsFromMongo(Long groupId, int limit) {
        return chatRepository.findByGroupIdOrderByTimestampAsc(groupId)
                .take(limit)
                .collectList();
    }
}
