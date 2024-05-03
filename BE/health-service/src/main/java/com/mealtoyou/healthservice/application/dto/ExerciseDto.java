package com.mealtoyou.healthservice.application.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExerciseDto {
	private Long steps;
	private LocalDate stepStartDate;
	private Double caloriesBurned;
	private LocalDate caloriesStartDate;
}
