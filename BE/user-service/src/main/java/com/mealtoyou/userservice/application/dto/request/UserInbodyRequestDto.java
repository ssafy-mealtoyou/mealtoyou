package com.mealtoyou.userservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInbodyRequestDto {
	@NotNull(message = "skeletalMuscle is empty.")
	private Integer skeletalMuscle;
	@NotNull(message = "bodyFat is empty.")
	private Integer bodyFat;
}
