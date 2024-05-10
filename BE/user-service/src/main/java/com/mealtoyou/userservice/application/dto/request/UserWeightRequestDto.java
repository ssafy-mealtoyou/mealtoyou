package com.mealtoyou.userservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record UserWeightRequestDto(
	@NotNull(message = "weight is empty.")
	Integer weight
) {
	@Builder
	public UserWeightRequestDto {
	}
}
