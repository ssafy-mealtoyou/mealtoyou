package com.mealtoyou.chattingservice.application.service;

import com.mealtoyou.chattingservice.application.dto.CommunityDietDTO;
import com.mealtoyou.chattingservice.domain.model.Chat;
import com.mealtoyou.chattingservice.domain.model.CommunityDietMessage;
import com.mealtoyou.chattingservice.domain.repository.ChatRepository;
import com.mealtoyou.chattingservice.infrastructure.kafka.KafkaMonoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteService {

    private final ChatRepository chatRepository;
    private final KafkaMonoUtils kafkaMonoUtils;

    public Mono<Chat> routeChat(Chat chat) {
        // 채팅 메시지를 MongoDB에 저장
        Mono<Chat> saveChatMono = chatRepository.save(chat);

        // 메시지가 CommunityDietMessage 인지 확인
        if (chat.getMessage() instanceof CommunityDietMessage) {
            log.info("kafka community diet message");
            // CommunityDietMessage 인 경우, Kafka 로 전송
            return saveChatMono.flatMap(savedChat ->
                    kafkaMonoUtils.sendAndReceive(
                                    "community-service-diet",
                                    new CommunityDietDTO(
                                            chat.getGroupId(),
                                            ((CommunityDietMessage) savedChat.getMessage()).getDailyDietsResponseDto().diets().get(0).dietId()
                                    )
                            )
                            .then(Mono.just(savedChat))
            );
        } else {
            // CommunityDietMessage 가 아닌 경우, 저장된 채팅을 반환
            return saveChatMono;
        }
    }


}
