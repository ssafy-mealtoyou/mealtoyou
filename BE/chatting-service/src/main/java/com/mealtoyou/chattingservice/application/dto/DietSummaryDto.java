package com.mealtoyou.chattingservice.application.dto;

import lombok.Builder;

import java.util.List;

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
