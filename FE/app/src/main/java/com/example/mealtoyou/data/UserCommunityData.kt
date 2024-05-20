package com.example.mealtoyou.data

import com.example.mealtoyou.ui.theme.diet.Diet


data class UserCommunityData(
    val title: String,
    val cntUsers: Int,
    val dailyGoalCalories: Int,
    val dailyGoalSteps: Int,
    val weeklyMinGoal: Int,
    val weeklyRemainGoal: Int,
    val isToday: Boolean,
    val steps: Int,
    val caloriesBurned: Int,
    val communityDietList: List<Diet>
)
