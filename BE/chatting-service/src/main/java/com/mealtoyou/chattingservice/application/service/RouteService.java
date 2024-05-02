package com.mealtoyou.chattingservice.application.service;

import com.mealtoyou.chattingservice.domain.model.Chat;
import com.mealtoyou.chattingservice.domain.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteService {

    private final KafkaTemplate<Object, String> kafkaTemplate;
    private final SimpMessageSendingOperations messageSendingOperations;
    private final ChatRepository chatRepository;

    public Mono<String> route(String message, Long userId, Long groupId) {
        log.info("Received message: {}", message);

        Chat chat = new Chat();
        chat.setUserId(userId);
        chat.setGroupId(groupId);
        chat.setMessage(message);
        chat.setTimestamp(LocalDateTime.now());

        // Save the chat message to MongoDB
        return chatRepository.save(chat)
                .flatMap(savedChat -> Mono.fromRunnable(() -> {
                    kafkaTemplate.send("group-chat-topic", message);
//                    messageSendingOperations.convertAndSend("/group-chat", message);
                }).thenReturn(message));
    }
}