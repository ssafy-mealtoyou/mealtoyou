package com.example.mealtoyou.api

import com.example.mealtoyou.data.model.response.UserHealthResDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApiService {
    @PUT("api/user-service/users/weight")
    fun putUserWeight(@Body weight: Map<String, String>): Call<Void>

    @PUT("api/user-service/users/intermittent")
    fun putUserIntermittent(@Body weight: Map<String, String>): Call<Void>

    @GET("api/user-service/users/health")
    suspend fun getUserHealth(): Response<UserHealthResDto>
}