package com.mealtoyou.communityservice.presentation.request;


import jakarta.validation.constraints.*;

public record CreateCommunityRequest(
        @NotBlank(message = "title은 필수 값 입니다.")
        String title,

        @NotNull(message = "period는 필수 값 입니다.")
        @Min(value = 30, message = "최소 30일 이상이여야 합니다.")
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
}
