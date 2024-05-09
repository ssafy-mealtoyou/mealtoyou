package com.example.mealtoyou.ui.theme.group

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
    val message: String,
    val community: String
)
