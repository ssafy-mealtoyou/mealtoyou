package com.mealtoyou.communityservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TodaysGoalDTO {
    private int weeklyRemainGoal;
    private boolean isToday;
}
