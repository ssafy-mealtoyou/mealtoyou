package com.mealtoyou.userservice.application.dto.request;

import lombok.Builder;

public record UserInbodyRequestDto(
	Integer boneMuscle,
	Integer bodyFat
) {
	@Builder
	public UserInbodyRequestDto {
	}
}
