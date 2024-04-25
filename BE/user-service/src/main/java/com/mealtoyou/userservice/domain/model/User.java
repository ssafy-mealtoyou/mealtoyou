package com.mealtoyou.userservice.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

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

    @Column("intermittent_fasting_yn")
    private boolean isIntermittentFasting;

    @Column("user_image_url")
    private String userImageUrl;

    @Column("goal_weight")
    private Double goalWeight;

    @Column("goal_end_date")
    private LocalDateTime goalEndDate;

    @Column("withdraw_yn")
    private boolean isWithdraw;

    private String role;

}
