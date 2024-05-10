package com.mealtoyou.healthservice.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInbodyRequestDto {
	private String token;
	private Integer skeletalMuscle;
	private Integer bodyFat;
	private Double weight;
	private Double height;
	private Integer age;
}
