package com.mealtoyou.chattingservice.presentation.controller;

import com.mealtoyou.chattingservice.domain.model.Chat;
import com.mealtoyou.chattingservice.domain.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public Mono<Chat> sendMessage(@RequestBody Chat chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatRepository.save(chatMessage)
                .doOnNext(savedMessage -> messagingTemplate.convertAndSend("/topic/group-chat", savedMessage));
    }

    @GetMapping("/messages")
    public Flux<Chat> getAllMessages() {
        return chatRepository.findAll();
    }
}