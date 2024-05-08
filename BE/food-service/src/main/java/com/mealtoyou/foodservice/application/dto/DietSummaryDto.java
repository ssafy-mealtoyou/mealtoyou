package com.mealtoyou.foodservice.application.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
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
