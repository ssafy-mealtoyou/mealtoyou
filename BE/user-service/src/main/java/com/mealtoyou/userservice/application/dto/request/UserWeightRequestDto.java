package com.mealtoyou.userservice.application.dto.request;

import lombok.Builder;

public record UserWeightRequestDto(
	Integer weight
) {
	@Builder
	public UserWeightRequestDto {
	}
}
