package com.mealtoyou.foodservice.application.dto;

import lombok.Builder;

public record CommunityDietFoodDto(
	String foodName,
	String foodImageUrl,
	Integer calories
) {
	@Builder
	public CommunityDietFoodDto {
	}
}
