package com.mealtoyou.userservice.application.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record DailyExerciseRequestDto(
	Long userId,
	LocalDate date
) {
}
