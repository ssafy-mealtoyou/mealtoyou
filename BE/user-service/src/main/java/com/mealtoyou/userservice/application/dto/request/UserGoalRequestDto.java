package com.mealtoyou.userservice.application.dto.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;


public record UserGoalRequestDto(
	@NotNull(message = "goalWeight is empty.")
	Integer goalWeight,
	// @NotNull(message = "goalEndDate is empty.")
	// @JsonFormat(pattern = "yyyy-MM-dd")
	// LocalDate goalEndDate
	@NotNull(message = "goalEndDate is empty.")
	String goalEndDate
) {
	@Builder
	public UserGoalRequestDto {
	}

	public LocalDate getParsedGoalEndDate() {
		return LocalDate.parse(goalEndDate, DateTimeFormatter.ISO_DATE);
	}
}
