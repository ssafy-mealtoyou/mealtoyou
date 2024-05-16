package com.mealtoyou.userservice.application.dto.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserHomeResponseDto {

	private DaySummary daySummary;

	private Integer goalWeight;

	private LocalDate goalEndDate;

	private Double goalStartWeight;

	private LocalDate goalStartDate;

	private Double currentWeight;

	@Getter
	@Builder
	public static class DaySummary {
		private Integer dietPer;
		private Integer caloriesPer;
		private Integer activityPer;
	}
}
