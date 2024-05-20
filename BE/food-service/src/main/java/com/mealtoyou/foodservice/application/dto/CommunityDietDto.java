package com.mealtoyou.foodservice.application.dto;

import java.util.List;

import lombok.Builder;

public record CommunityDietDto(
        Long dietId,

        Integer totalCalories,

        Integer carbohydratePer,

        Integer proteinPer,

        Integer fatPer,
        List<CommunityDietFoodDto> dietFoods
) {
    @Builder
    public CommunityDietDto {
    }
}
