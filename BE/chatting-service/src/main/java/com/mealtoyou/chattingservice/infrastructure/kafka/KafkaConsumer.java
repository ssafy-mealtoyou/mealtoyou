package com.mealtoyou.chattingservice.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.chattingservice.domain.model.Chat;
import com.mealtoyou.chattingservice.infrastructure.handler.ChatWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final ObjectMapper objectMapper;

//    @KafkaListener(topics = "group-chat-topic", groupId = "group-chat")
//    public Mono<Void> listen(String chat) {
//        log.info("Kafka listen: {}", chat);
//        Chat receivedChat = deserializeChat(chat);
//        log.info("Deserialized chat: {}", receivedChat);
//        return chatWebSocketHandler.broadcastMessageToGroup(receivedChat);
//    }

    private Chat deserializeChat(String chat) {
        try {
            return objectMapper.readValue(chat, Chat.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing chat from JSON", e);
        }
    }
}
