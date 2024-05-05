package com.mealtoyou.userservice.application.dto.request;

import java.time.LocalTime;

import lombok.Builder;

public record UserIntermittentFastingRequestDto(
	Boolean intermittentFastingYn,
	LocalTime intermittentStartTime,
	LocalTime intermittentEndTime
) {
	@Builder
	public UserIntermittentFastingRequestDto {
	}
}
