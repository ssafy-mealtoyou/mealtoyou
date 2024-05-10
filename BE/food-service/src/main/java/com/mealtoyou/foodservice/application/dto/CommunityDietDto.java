package com.mealtoyou.foodservice.application.dto;

import java.util.List;

import lombok.Builder;

public record CommunityDietDto(
	String nickname,
	Integer totalCalories,
	Integer carbohydratePer,
	Integer proteinPer,
	Integer fatPer,
	List<CommunityDietFoodDto> foodList
) {
	@Builder
	public CommunityDietDto {
	}
}
