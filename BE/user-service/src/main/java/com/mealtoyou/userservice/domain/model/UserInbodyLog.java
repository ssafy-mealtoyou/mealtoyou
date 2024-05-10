package com.mealtoyou.userservice.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Table("user_inbody_log")
public class UserInbodyLog {
	@Id
	@Column("user_inbody_log_id")
	private Integer userInbodyLogId;

	@Column("user_id")
	private Long userId;

	@Column("body_fat")
	private Integer bodyFat;

	@Column("skeletal_muscle")
	private Integer skeletalMuscle;

	@Column("create_date")
	private LocalDateTime createDate;
}
