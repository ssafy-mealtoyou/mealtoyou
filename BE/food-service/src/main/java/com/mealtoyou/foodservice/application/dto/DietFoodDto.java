package com.mealtoyou.foodservice.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
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
