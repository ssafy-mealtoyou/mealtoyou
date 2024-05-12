package com.mealtoyou.chattingservice.application.dto;

import lombok.Builder;

import java.util.List;

public record DailyDietsResponseDto(
        String type,
        List<DietSummaryDto> diets
) {
    @Builder
    public DailyDietsResponseDto {
    }
}
