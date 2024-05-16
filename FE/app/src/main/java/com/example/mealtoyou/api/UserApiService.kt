package com.example.mealtoyou.api

import com.example.mealtoyou.data.UserGoalRequestData
import com.example.mealtoyou.data.model.response.UserHealthResDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import com.example.mealtoyou.data.UserHomeResponse

interface UserApiService {
    @GET("api/user-service/users/home")
    suspend fun getUserHomeSummary() : Response<UserHomeResponse>

    @PUT("api/user-service/users/weight")
    fun putUserWeight(@Body weight: Map<String, String>): Call<Void>

    @PUT("api/user-service/users/intermittent")
    fun putUserIntermittent(@Body weight: Map<String, String>): Call<Void>

    @GET("api/user-service/users/health")
    suspend fun getUserHealth(): Response<UserHealthResDto>

    @PUT("api/user-service/users/goal")
    fun updateGoal(
        @Body requestDto: UserGoalRequestData
    ): Call<Void>
}