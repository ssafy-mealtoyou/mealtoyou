package com.example.mealtoyou.data

import java.time.LocalDateTime

data class FcmData (
    val fcmToken: String,
    val timeStamp: LocalDateTime,
)