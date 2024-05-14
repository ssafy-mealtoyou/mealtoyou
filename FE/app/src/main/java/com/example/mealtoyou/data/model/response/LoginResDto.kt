package com.example.mealtoyou.data.model.response

data class LoginResDto(
    val accessToken:String,
    val refreshToken:String,
    val userId:String,
)