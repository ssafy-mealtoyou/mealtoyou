package com.mealtoyou.userservice.application.dto;

import lombok.Builder;

@Builder
public record DailyDietFoodRequestDto(
	Long userId,
	String date
) {
}
