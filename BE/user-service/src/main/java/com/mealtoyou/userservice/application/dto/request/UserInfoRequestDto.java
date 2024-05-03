package com.mealtoyou.userservice.application.dto.request;

import java.time.LocalDateTime;

import com.mealtoyou.userservice.domain.model.User;

import lombok.Builder;

@Builder
public record UserInfoRequestDto(
	String nickname,
	boolean gender,
	int age,
	double height,
	double weight
) {
	public static UserInfoRequestDto fromEntity(User user) {
		return UserInfoRequestDto.builder()
			.nickname(user.getNickname())
			.gender(user.isGender())
			.age(user.getAge())
			.height(user.getHeight())
			.weight(user.getWeight())
			.build();
	}
}
