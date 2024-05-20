package com.mealtoyou.userservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserInbodyRequestDto {
	private String token;
	@NotNull(message = "skeletalMuscle is empty.")
	private Integer skeletalMuscle;
	@NotNull(message = "bodyFat is empty.")
	private Integer bodyFat;
	private Double weight;
	private Double height;
	private Integer age;

	public void setOthers(String token, double weight, double height, int age) {
		this.token = token;
		this.weight = weight;
		this.height = height;
		this.age = age;
	}
}
