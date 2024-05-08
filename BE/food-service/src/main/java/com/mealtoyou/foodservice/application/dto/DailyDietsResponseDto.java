package com.mealtoyou.foodservice.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public record DailyDietsResponseDto(
	String date,
	Double dailyCaloriesBurned,
	Double dailyCarbohydrateTaked,
	Double dailyProteinTaked,
	Double dailyFatTaked,
	List<DietSummaryDto> diets
) {
	@Builder
	public DailyDietsResponseDto {
	}
}
