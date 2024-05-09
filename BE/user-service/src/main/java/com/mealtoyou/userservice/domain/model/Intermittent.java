package com.mealtoyou.userservice.domain.model;

import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.mealtoyou.userservice.application.dto.request.IntermittentRequestDto;

import lombok.Builder;
import lombok.Getter;

@Table("intermittent_fasting_timer")
@Getter
@Builder
public class Intermittent {
	@Id
	@Column("intermittent_fasting_timer_id")
	private Long intermittentId;
	@Column("user_id")
	private Long userId;
	@Column("start_time")
	private LocalTime startTime;
	@Column("end_time")
	private LocalTime endTime;

	public void updateTime(IntermittentRequestDto intermittentRequestDto) {
		this.startTime = intermittentRequestDto.getStartTime();
		this.endTime = intermittentRequestDto.getEndTime();
	}
}
