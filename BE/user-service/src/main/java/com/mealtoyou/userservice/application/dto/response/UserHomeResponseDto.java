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

	@Getter
	@Builder
	public static class DaySummary {
		private Integer dietPer;
		private Integer carbohydratePer;
		private Integer proteinPer;
		private Integer fatPer;
		private Integer activityPer;
	}
}
