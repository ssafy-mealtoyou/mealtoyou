package com.mealtoyou.communityservice.presentation.response;

import com.mealtoyou.communityservice.domain.model.Community;

public record CommunityResponse(
        Long communityId,
        String title,
        int cntUsers,
        int dailyGoalCalories,
        int dailyGoalSteps,
        int weeklyMinGoal
) {
    public static CommunityResponse toEntity(Community community) {
        return new CommunityResponse(
                community.getCommunityId(),
                community.getTitle(),
                community.getCntUsers(),
                community.getDailyGoalCalories(),
                community.getDailyGoalSteps(),
                community.getWeeklyMinGoal()
        );
    }
}
