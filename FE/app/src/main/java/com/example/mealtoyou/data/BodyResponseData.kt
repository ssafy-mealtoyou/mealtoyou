package com.example.mealtoyou.data

data class BodyResponseData(
    val bmr: Double,
    val measuredDate: String,
    val weight: Double,
    val bodyFat: Double,
    val skeletalMuscle: Double,
    val bmi: Double
)
