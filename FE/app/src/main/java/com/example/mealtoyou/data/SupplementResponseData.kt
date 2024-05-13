package com.example.mealtoyou.data

import java.time.LocalTime

data class SupplementResponseData(
    val supplementId : Long,
    val name : String,
    val takenYn : Boolean,
    val alertTime : String,
    )
