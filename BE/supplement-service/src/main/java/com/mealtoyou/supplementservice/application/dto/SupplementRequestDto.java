package com.mealtoyou.supplementservice.application.dto;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SupplementRequestDto {
	private String name;
	private Boolean takenYn;
	private String alertTime;

	// public LocalTime getAlertTimeAsLocalTime() {
	// 	return LocalTime.parse(alertTime, DateTimeFormatter.ofPattern("HH:mm"));
	// }
}
