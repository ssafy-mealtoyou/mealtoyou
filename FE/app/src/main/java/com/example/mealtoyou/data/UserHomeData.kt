package com.example.mealtoyou.data


data class UserHomeResponse(
    val daySummary: DaySummary = DaySummary(),
    val goalWeight: Int = 0,
    val goalEndDate: String = "",
    val goalStartWeight: Double = 0.0,
    val goalStartDate: String = "",
    val currentWeight: Double = 0.0
)

data class DaySummary(
    val dietPer: Int = 0,
    val caloriesPer: Int = 0,
    val activityPer: Int = 0,
)
