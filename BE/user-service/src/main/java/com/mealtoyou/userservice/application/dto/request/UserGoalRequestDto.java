package com.mealtoyou.userservice.application.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record UserGoalRequestDto(
	@NotNull(message = "goalWeight is empty.")
	Integer goalWeight,
	@NotNull(message = "goalEndDate is empty.")
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate goalEndDate
) {
	@Builder
	public UserGoalRequestDto {
	}
}
