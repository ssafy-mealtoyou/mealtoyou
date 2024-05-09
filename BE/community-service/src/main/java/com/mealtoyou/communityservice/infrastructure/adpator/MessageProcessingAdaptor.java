package com.mealtoyou.communityservice.infrastructure.adpator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.communityservice.application.dto.CommunityDietDTO;
import com.mealtoyou.communityservice.domain.model.CommunityDiet;
import com.mealtoyou.communityservice.domain.repository.CommunityDietRepository;
import com.mealtoyou.communityservice.infrastructure.kafka.KafkaMessageEnable;
import com.mealtoyou.communityservice.infrastructure.kafka.KafkaMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@KafkaMessageEnable
@RequiredArgsConstructor
public class MessageProcessingAdaptor {

    private final CommunityDietRepository communityDietRepository;
    private final ObjectMapper objectMapper;

    @KafkaMessageListener(topic = "diet")
    public Mono<CommunityDiet> processMessage1(String message) {
        CommunityDietDTO communityDietDTO = objectMapper.convertValue(message, CommunityDietDTO.class);
        CommunityDiet communityDiet = CommunityDiet.builder()
                .communityId(communityDietDTO.getCommunityId())
                .dietId(communityDietDTO.getDietId())
                .build();
        return communityDietRepository.save(communityDiet);
    }

    @KafkaMessageListener(topic = "requests2")
    public String processMessage2(String message) {
        return message + "2번 MSA입니다.";
    }

}
