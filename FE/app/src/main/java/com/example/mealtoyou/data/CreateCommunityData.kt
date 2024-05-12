package com.example.mealtoyou.data

import java.time.LocalDate

data class CreateCommunityData(
    val title: String,
    val period: Int,
    val dailyGoalCalories: Int,
    val dailyGoalSteps: Int,
    val weeklyMinGoal: Int
)