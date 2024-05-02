package com.mealtoyou.healthservice.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;

@Table("exercise")
@Getter
@Builder
public class Exercise {
	@Id
	@Column("exercise_id")  // 데이터베이스에서의 정확한 컬럼 이름 지정
	private Long exerciseId;

	@Column("user_id")
	private Long userId;

	@Column("steps")
	private Long steps;

	@Column("calories_burned")
	private Double caloriesBurned;

	@Column("step_start_date")
	private LocalDate stepStartDate;

	@Column("calories_start_date")
	private LocalDate caloriesStartDate;
}
