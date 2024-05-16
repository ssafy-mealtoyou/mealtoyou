package com.mealtoyou.userservice.application.dto.response;

import java.time.LocalDate;

import com.mealtoyou.userservice.domain.model.User;

import lombok.Builder;

@Builder
public record UserInfoResponseDto(
	long userId,
	String email,
	String nickname,
	boolean gender,
	int age,
	double height,
	double weight,
	boolean isIntermittentFasting,
	String userImageUrl,
	Integer goalWeight,
	LocalDate goalEndDate,
	Double goalStartWeight,
	LocalDate goalStartDate,
	String role
) {
	public static UserInfoResponseDto fromEntity(User user) {
		return UserInfoResponseDto.builder()
			.userId(user.getUserId())
			.email(user.getEmail())
			.nickname(user.getNickname())
			.gender(user.isGender())
			.age(user.getAge())
			.height(user.getHeight())
			.weight(user.getWeight())
			.isIntermittentFasting(user.isIntermittentFasting())
			.userImageUrl(user.getUserImageUrl())
			.goalWeight(user.getGoalWeight())
			.goalEndDate(user.getGoalEndDate())
			.goalStartWeight(user.getGoalStartWeight())
			.goalStartDate(user.getGoalStartDate())
			.role(user.getRole())
			.build();
	}
}
