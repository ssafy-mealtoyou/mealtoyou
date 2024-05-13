package com.mealtoyou.userservice.application.dto;

import java.util.List;

import lombok.Builder;

public record DietSummaryDto(
	Long dietId,
	Integer totalCalories,
	Integer carbohydratePer,
	Integer proteinPer,
	Integer fatPer,
	List<DietFoodDto> dietFoods
) {
	@Builder
	public DietSummaryDto {
	}
}
