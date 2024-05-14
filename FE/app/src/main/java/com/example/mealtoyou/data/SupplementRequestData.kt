package com.example.mealtoyou.data

import java.time.LocalTime


data class SupplementRequestData(
    val name: String? = null,
    val takenYn: Boolean? = null,
    val alertTime: LocalTime  ? = null
)