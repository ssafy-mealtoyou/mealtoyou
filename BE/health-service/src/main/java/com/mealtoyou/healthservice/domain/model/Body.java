package com.mealtoyou.healthservice.domain.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;

@Table("user_health")
@Getter
@Builder(toBuilder = true)
public class Body {
	@Id
	@Column("user_health_id")
	private Long userHealthId;

	@Column("user_id")
	private Long userId;

	@Column("measured_date")
	private LocalDate measuredDate;

	@Column("weight")
	private Double weight;

	@Column("body_fat")
	private Double bodyFat;

	@Column("bmr")
	private Double bmr;

	@Column("bmi")
	private Double bmi;

	@Column("skeletal_muscle")
	private Double skeletalMuscle;


}
