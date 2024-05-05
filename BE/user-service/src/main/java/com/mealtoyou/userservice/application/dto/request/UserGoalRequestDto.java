package com.mealtoyou.userservice.application.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;

public record UserGoalRequestDto(
	Integer goalWeight,
	LocalDateTime goalEndDate
) {
	@Builder
	public UserGoalRequestDto {
	}
}
