package com.mealtoyou.foodservice.infrastructor.adpator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.foodservice.application.dto.CommunityDietDto;
import com.mealtoyou.foodservice.application.dto.CommunityDietsRequestDto;
import com.mealtoyou.foodservice.application.service.DietService;
import com.mealtoyou.foodservice.infrastructor.kafka.KafkaMessageEnable;
import com.mealtoyou.foodservice.infrastructor.kafka.KafkaMessageListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@KafkaMessageEnable
@RequiredArgsConstructor
@Slf4j
public class MessageProcessingAdaptor {

    private final ObjectMapper objectMapper;

    private final DietService dietService;

    private CommunityDietsRequestDto readCommunityDietsRequestDto(String message) throws JsonProcessingException {
        CommunityDietsRequestDto dto = objectMapper.readValue(message, CommunityDietsRequestDto.class);
        if (dto.getDietIdList() == null || dto.getUserId() == null) {
            throw new IllegalArgumentException("유효하지 않은 요청값입니다. ");
        }
        return dto;
    }

    @KafkaMessageListener(topic = "diets")
    public Mono<List<CommunityDietDto>> processMessage1(String message) throws JsonProcessingException {
        log.info("커뮤니티 다이어트 목록 조회: {}", message);
        CommunityDietsRequestDto dto = readCommunityDietsRequestDto(message);
        return dietService.getCommunityDiets(dto.getUserId(), dto.getDietIdList());
    }

    @KafkaMessageListener(topic = "requests2")
    public String processMessage2(String message) {
        return message + "2번 MSA입니다.";
    }

}
