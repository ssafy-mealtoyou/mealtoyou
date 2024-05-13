package com.example.mealtoyou.data

data class CommunityData(
    val communityId: Long,
    val title: String,
    val cntUsers: Int,
    val dailyGoalCalories: Int,
    val dailyGoalSteps: Int,
    val weeklyMinGoal: Int
)