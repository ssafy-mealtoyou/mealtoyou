package com.mealtoyou.foodservice.application.dto;

public record DailyDietFoodRequestDto(
	Long userId,
	String date
) {
}
