package com.mealtoyou.userservice.application.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FcmRequestDto {
	String fcmToken;
	LocalDateTime timestamp;
}
