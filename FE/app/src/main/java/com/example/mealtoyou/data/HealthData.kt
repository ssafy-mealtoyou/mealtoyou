package com.example.mealtoyou.data

import java.time.LocalDate
import java.util.Date

data class HealthData(val bmr: Double, val measuredDate: LocalDate, val bodyFat: Double, val skeletalMuscle: Double, val weight: Double)
