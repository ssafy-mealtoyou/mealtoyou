package com.mealtoyou.foodservice.application.dto;

import lombok.Builder;

public record DietFoodDto(
	String name,
	String imageUrl,
	Double calories,
	Double carbohydrate,
	Double protein,
	Double fat
) {
	@Builder
	public DietFoodDto {
	}
}
