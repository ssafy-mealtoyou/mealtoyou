package com.mealtoyou.chattingservice.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.chattingservice.domain.model.Chat;
import com.mealtoyou.chattingservice.domain.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteService {

    private final KafkaTemplate<Object, String> kafkaTemplate;
    private final ChatRepository chatRepository;
    private final ObjectMapper objectMapper;

    public Mono<Chat> route(Chat chat) {
        // Save the chat message to MongoDB and send it to Kafka
        return chatRepository.save(chat)
                .flatMap(savedChat -> {
                    // Send the chat message to Kafka as JSON
                    String jsonChat = convertChatToJson(savedChat);
                    kafkaTemplate.send("group-chat-topic", jsonChat);
                    // Finish by returning the saved chat
                    return Mono.just(savedChat);
                });
    }

    private String convertChatToJson(Chat chat) {
        try {
            return objectMapper.writeValueAsString(chat);
        } catch (JsonProcessingException e) {
            log.error("Error converting chat to JSON: {}", e.getMessage());
            throw new RuntimeException("Error converting chat to JSON");
        }
    }
}
