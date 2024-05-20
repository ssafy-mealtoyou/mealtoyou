package com.mealtoyou.healthservice.application.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BodyDto {
	private Double bmr;
	private LocalDate measuredDate;
	private Double weight;
	private Double bodyFat;
	private Double skeletalMuscle;
	private Double bmi;
}
