package com.mealtoyou.communityservice.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("community")
@Getter
@Builder
public class Community {

    @Id
    @Column("community_id")
    private Long communityId;

    @Column("leader_id")
    private Long leaderId;

    private String title;

    @Column("cnt_users")
    private Integer cntUsers;

    @Column("start_date")
    private LocalDateTime startDate;

    @Column("end_date")
    private LocalDateTime endDate;

    @Column("daily_goal_calories")
    private Integer dailyGoalCalories;

    @Column("daily_goal_steps")
    private Integer dailyGoalSteps;

    @Column("weekly_min_goal")
    private Integer weeklyMinGoal;

}