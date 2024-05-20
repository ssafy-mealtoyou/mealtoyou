package com.example.mealtoyou.data.model.response

data class UserHealthResDto(
    val nickname: String,
    val imageUrl: String,
    val inbodyBoneMuscle: Double,
    val inbodyBodyFat: Double,
    val intermittentYn: Boolean,
    val intermittentStartTime: String,
    val intermittentEndTime: String,
    val weight: Double,
    val weightLastMonth: Double,
    val weightThisYear: Double,
    val goalWeight: Double,
    val goalDate: String
)