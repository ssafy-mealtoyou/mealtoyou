package com.mealtoyou.userservice.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MessageRequestDto {
	private String title;
	private String body;
	private String targetToken;
}
