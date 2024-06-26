package com.mealtoyou.supplementservice.domain.model;

import java.time.LocalTime;

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
	private LocalTime alertTime;

	@Column("taken_yn")
	private Boolean takenYn;

	public void updateSupplementTakenYn(boolean b) {
		this.takenYn = b;
	}
}
