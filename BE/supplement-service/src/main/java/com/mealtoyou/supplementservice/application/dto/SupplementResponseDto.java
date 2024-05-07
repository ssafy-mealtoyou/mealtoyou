package com.mealtoyou.supplementservice.application.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SupplementResponseDto {
	private Long supplementId;
	private String name;
	private Boolean takenYn;
	private LocalDateTime alertTime;
}
