package com.mealtoyou.healthservice.application.dto;

import java.time.LocalDate;

public record DailyExerciseRequestDto(
	Long userId,
	LocalDate date
) {
}
