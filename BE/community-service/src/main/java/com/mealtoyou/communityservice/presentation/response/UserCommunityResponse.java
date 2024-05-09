package com.mealtoyou.communityservice.presentation.response;

import com.mealtoyou.communityservice.application.dto.UserHealthInfo;
import com.mealtoyou.communityservice.domain.model.Community;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
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

        List<CommunityDietResponse> communityDietList

//        List<String> recentMessageList
) {

    public static UserCommunityResponse toEntity(Community community,
                                                 List<CommunityDietResponse> sharedMenuList, Integer weeklyRemainGoal,
                                                 UserHealthInfo userHealthInfo) {
        log.info("start");

        // null 값을 빈 문자열로 대체
        sharedMenuList = sharedMenuList != null ? sharedMenuList : Collections.emptyList();

        return new UserCommunityResponse(
                community != null ? community.getTitle() : "",  // null 값이면 빈 문자열("")로 대체
                community != null ? community.getCntUsers() : 0,  // null 값이면 0으로 대체
                community != null ? community.getStartDate() : null,  // null 값이면 null 그대로 사용
                community != null ? community.getEndDate() : null,  // null 값이면 null 그대로 사용
                community != null ? community.getDailyGoalCalories() : 0,  // null 값이면 0으로 대체
                community != null ? community.getDailyGoalSteps() : 0,  // null 값이면 0으로 대체
                community != null ? community.getWeeklyMinGoal() : 0,  // null 값이면 0으로 대체
                weeklyRemainGoal != null ? weeklyRemainGoal : 0,  // null 값이면 0으로 대체
                userHealthInfo != null ? userHealthInfo.getSteps() : 0,  // null 값이면 0으로 대체
                userHealthInfo != null ? userHealthInfo.getCaloriesBurned() : 0,  // null 값이면 0으로 대체
                sharedMenuList
//                recentChatContents
        );
    }


}
