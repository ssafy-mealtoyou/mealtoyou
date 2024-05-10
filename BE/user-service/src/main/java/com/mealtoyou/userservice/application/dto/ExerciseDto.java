package com.mealtoyou.userservice.application.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ExerciseDto {
	private Long steps;
	private LocalDate stepStartDate;
	private Double caloriesBurned;
	private LocalDate caloriesStartDate;
}
