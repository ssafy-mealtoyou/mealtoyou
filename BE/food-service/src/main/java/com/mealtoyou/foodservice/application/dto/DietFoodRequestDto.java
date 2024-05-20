package com.mealtoyou.foodservice.application.dto;

import lombok.Builder;

public record DietFoodRequestDto(
	Long foodId,
	Double servingSize
) {
	@Builder
	public DietFoodRequestDto {
	}
}
