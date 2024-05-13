package com.mealtoyou.supplementservice.application.dto;

import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SupplementRequestDto {
	private String name;
	private Boolean takenYn;
	private LocalTime alertTime;
}
