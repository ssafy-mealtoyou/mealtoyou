package com.mealtoyou.chattingservice.infrastructure.kafka;

import com.mealtoyou.chattingservice.domain.model.Chat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final SimpMessageSendingOperations messageSendingOperations;

    @KafkaListener(topics = "group-chat-topic", groupId = "group-chat")
    public void listen(String chat) {
        log.info("Kafka listen: {}", chat);
        messageSendingOperations.convertAndSend("/group-chat", chat);
    }

}
