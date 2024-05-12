package com.example.mealtoyou.ui.theme.group

import com.example.mealtoyou.ui.theme.diet.DailyDietsResponseDto

data class ChatMessage(
    val id: Id,
    val userId: Int,
    val groupId: Int,
    val message: Message,
    val timestamp: String
)

data class Id(
    val timestamp: Long,
    val date: String
)

data class Message(
    val dailyDietsResponseDto: DailyDietsResponseDto?,
    val message: String?
)

data class SendMessage(
    val dailyDietsResponseDto: DailyDietsResponseDto?,
    val type: String
)