package com.mealtoyou.userservice.application.dto;

import java.util.List;

import lombok.Builder;

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
