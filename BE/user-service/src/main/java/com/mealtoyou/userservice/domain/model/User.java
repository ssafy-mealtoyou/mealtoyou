package com.mealtoyou.userservice.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

import com.mealtoyou.userservice.application.dto.request.UserInfoRequestDto;

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
    private Double goalWeight;

    @Column("goal_end_date")
    private LocalDateTime goalEndDate;

    @Column("withdraw_yn")
    private boolean isWithdraw;

    private String role;

    public void updateUserInfo(UserInfoRequestDto u,String imageUrl) {
        this.nickname = u.nickname();
        this.gender = u.gender();
        this.age = u.age();
        this.height = u.height();
        this.weight = u.weight();
        if(imageUrl!=null){
            this.userImageUrl = imageUrl;
        }
        // this.isIntermittentFasting = u.isIntermittentFasting();
        // this.goalWeight = u.goalWeight();
        // this.goalEndDate = u.goalEndDate();
    }

}
