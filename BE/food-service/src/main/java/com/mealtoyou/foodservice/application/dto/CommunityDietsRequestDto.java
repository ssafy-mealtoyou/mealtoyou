package com.mealtoyou.foodservice.application.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class CommunityDietsRequestDto {
	private Integer userId;
	private List<Long> dietIdList;
}
