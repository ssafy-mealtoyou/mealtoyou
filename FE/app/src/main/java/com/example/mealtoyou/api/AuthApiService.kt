package com.example.mealtoyou.api

import com.example.mealtoyou.data.model.request.LoginReqDto
import com.example.mealtoyou.data.model.response.LoginResDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/auth/login")
    suspend fun login(@Body loginReqDto: LoginReqDto) : Response<LoginResDto>
}