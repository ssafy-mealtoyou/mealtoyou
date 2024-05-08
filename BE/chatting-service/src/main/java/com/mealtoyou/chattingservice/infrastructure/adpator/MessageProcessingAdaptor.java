package com.mealtoyou.chattingservice.infrastructure.adpator;


import com.mealtoyou.chattingservice.application.dto.RecentMessage;
import com.mealtoyou.chattingservice.domain.model.Chat;
import com.mealtoyou.chattingservice.domain.repository.ChatRepository;
import com.mealtoyou.chattingservice.infrastructure.kafka.KafkaMessageEnable;
import com.mealtoyou.chattingservice.infrastructure.kafka.KafkaMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@KafkaMessageEnable
@RequiredArgsConstructor
public class MessageProcessingAdaptor {

    private final ChatRepository chatRepository;

    @KafkaListener(topics = "recentChat")
    public Mono<RecentMessage> getRecentChats(String groupId) {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "timestamp"));
        // 최근 3개의 채팅 메시지 조회
        return chatRepository.findTop3ByGroupIdOrderByTimestampDesc(Long.parseLong(groupId), pageable)
                .collectList() // Flux<Chat>을 List<Chat>으로 변환
                .map(chats -> {
                    // 조회된 채팅 메시지에서 message 필드만 추출하여 RecentMessage 객체 생성
                    String message1 = !chats.isEmpty() ? chats.get(0).getMessage() : null;
                    String message2 = chats.size() >= 2 ? chats.get(1).getMessage() : null;
                    String message3 = chats.size() >= 3 ? chats.get(2).getMessage() : null;
                    return new RecentMessage(message1, message2, message3);
                });
    }

    @KafkaMessageListener(topic = "requests2")
    public String processMessage2(String message) {
        return message + "2번 MSA입니다.";
    }

}
