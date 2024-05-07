package com.mealtoyou.supplementservice.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;

@Table("supplement")
@Getter
@Builder(toBuilder = true)
public class Supplement {
	@Id
	@Column("supplement_id")
	private Long supplementId;

	@Column("user_id")
	private Long userId;

	private String name;

	@Column("alert_time")
	private LocalDateTime alertTime;

	@Column("taken_yn")
	private Boolean takenYn;
}
