package com.example.mealtoyou.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.UUID

data class SupplementInfo(
    val id: UUID = UUID.randomUUID(), // UUID 타입으로 자동 생성
    var name: MutableState<String> = mutableStateOf(""),
    var hour: MutableState<String> = mutableStateOf(""),
    var minute: MutableState<String> = mutableStateOf(""),
)
