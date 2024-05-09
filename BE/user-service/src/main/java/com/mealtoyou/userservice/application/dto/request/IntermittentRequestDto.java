package com.mealtoyou.userservice.application.dto.request;

import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IntermittentRequestDto {
	Boolean intermittentYn;
	LocalTime startTime;
	LocalTime endTime;

}
