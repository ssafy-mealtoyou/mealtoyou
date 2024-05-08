package com.example.mealtoyou.data

import java.time.LocalDate

data class ExerciseData(
    val steps: Long,
    val stepStartDate: LocalDate,
    val caloriesBurned: Double,
    val caloriesStartDate: LocalDate,
)
