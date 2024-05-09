package com.mealtoyou.chattingservice.application.service;

import com.mealtoyou.chattingservice.domain.model.Chat;
import com.mealtoyou.chattingservice.domain.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public Flux<Chat> getChatsByGroupId(Long groupId, int pageSize, String lastId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "_id"); // Sort by _id in descending order

        if (lastId != null && !lastId.isEmpty()) {
            return chatRepository.findByGroupIdAndIdLessThanOrderByTimestampDesc(groupId, new ObjectId(lastId), PageRequest.of(0, pageSize, sort));
        } else {
            return chatRepository.findByGroupIdOrderByTimestampAsc(groupId);
        }
    }
}