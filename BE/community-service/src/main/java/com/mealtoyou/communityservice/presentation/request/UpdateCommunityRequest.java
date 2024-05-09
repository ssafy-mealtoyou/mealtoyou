package com.mealtoyou.communityservice.presentation.request;

import com.mealtoyou.communityservice.domain.model.Community;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCommunityRequest(

        @NotNull(message = "period는 필수 값 입니다.")
        @Positive(message = "음수 값은 등록할 수 없습니다.")
        @Max(value = 1000, message = "최대 1000일 까지 등록 가능합니다.")
        Integer period,

        @NotNull(message = "목표 칼로리는 필수 값 입니다.")
        Integer dailyGoalCalories,

        @NotNull(message = "목표 걸음 수는 필수 값 입니다.")
        Integer dailyGoalSteps,

        @NotNull(message = "주당 인증 횟수는 필수 값 입니다.")
        @Min(value = 1, message = "최소 1일 이상이여야 합니다.")
        @Max(value = 7, message = "최대 7일 까지 등록 가능합니다.")
        Integer weeklyMinGoal

) {
    public static Community toEntity(Community community,
                                     UpdateCommunityRequest updateCommunityRequest) {
        return Community.builder()
                .communityId(community.getCommunityId())
                .leaderId(community.getLeaderId())
                .title(community.getTitle())
                .cntUsers(community.getCntUsers())
                .startDate(community.getStartDate())
                .endDate(community.getEndDate().plusDays(updateCommunityRequest.period()))
                .dailyGoalCalories(updateCommunityRequest.dailyGoalCalories())
                .dailyGoalSteps(updateCommunityRequest.dailyGoalSteps())
                .weeklyMinGoal(updateCommunityRequest.weeklyMinGoal())
                .build();
    }

}
