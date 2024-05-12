package com.mealtoyou.foodservice.application.dto;

import lombok.Builder;

public record CommunityDietFoodDto(
		String name,
		String imageUrl,
		Double calories,
		Double carbohydrate,
		Double protein,
		Double fat
) {
	@Builder
	public CommunityDietFoodDto {
	}
}
