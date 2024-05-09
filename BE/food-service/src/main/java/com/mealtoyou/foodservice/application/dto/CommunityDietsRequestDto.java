package com.mealtoyou.foodservice.application.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class CommunityDietsRequestDto {
	private Long userId;
	private List<Long> dietIdList;
}
