package com.mealtoyou.communityservice.presentation.response;

import com.mealtoyou.communityservice.domain.model.Community;

import java.time.LocalDateTime;
import java.util.List;

public record UserCommunityResponse(
        String title,

        Integer cntUsers,

        LocalDateTime startDate,

        LocalDateTime endDate,

        Integer dailyGoalCalories,

        Integer dailyGoalSteps,

        Integer weeklyMinGoal,

        Integer weeklyRemainGoal,

        Integer steps,

        Integer caloriesBurned,

        List<CommunityDietResponse> communityDietList,

        List<String> recentMessageList
) {
    public static UserCommunityResponse toEntity(Community community, List<String> recentChatContents,
                                                 List<CommunityDietResponse> sharedMenuList, Integer weeklyRemainGoal,
                                                 Integer steps, Integer caloriesBurned) {
        return new UserCommunityResponse(
                community.getTitle(),
                community.getCntUsers(),
                community.getStartDate(),
                community.getEndDate(),
                community.getDailyGoalCalories(),
                community.getDailyGoalSteps(),
                community.getWeeklyMinGoal(),
                weeklyRemainGoal,
                steps,
                caloriesBurned,
                sharedMenuList,
                recentChatContents
        );
    }

}
