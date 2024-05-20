package com.mealtoyou.foodservice.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
