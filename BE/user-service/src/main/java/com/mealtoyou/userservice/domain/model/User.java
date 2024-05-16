package com.mealtoyou.userservice.domain.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.mealtoyou.userservice.application.dto.request.FcmRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserGoalRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserInfoRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("users")
@Getter
@Builder
public class User {

	@Id
	@Column("user_id")
	private Long userId;

	private String email;

	private String nickname;

	@Column("social_key")
	private String socialKey;

	private boolean gender;

	private int age;

	private double height;

	private double weight;

	@Setter
	@Column("intermittent_fasting_yn")
	private boolean isIntermittentFasting;

	@Setter
	@Column("user_image_url")
	private String userImageUrl;

	@Column("goal_weight")
	private Integer goalWeight;

	@Column("goal_end_date")
	private LocalDate goalEndDate;

	@Column("withdraw_yn")
	private boolean isWithdraw;

	private String role;

	private Double goalStartWeight;

	private LocalDate goalStartDate;

	@Column("fcm_token")
	private String fcmToken;

	public void updateUserInfo(UserInfoRequestDto u, String imageUrl) {
		this.nickname = u.nickname();
		this.gender = u.gender();
		this.age = u.age();
		this.height = u.height();
		this.weight = u.weight();
		if (imageUrl != null) {
			this.userImageUrl = imageUrl;
		}
	}

	public void updateIntermittent(boolean b) {
		this.isIntermittentFasting = b;
	}

	public void updateGoal(UserGoalRequestDto dto) {
		this.goalStartWeight = this.weight;
		this.goalStartDate = LocalDate.now();
		this.goalWeight = dto.goalWeight();
		this.goalEndDate = dto.getParsedGoalEndDate();
	}

	public void updateWeight(Integer weight) {
		this.weight = weight;
	}

	public void updateUserFcmToken(FcmRequestDto fcmRequestDto) {
		this.fcmToken = fcmRequestDto.getFcmToken();
	}
}
