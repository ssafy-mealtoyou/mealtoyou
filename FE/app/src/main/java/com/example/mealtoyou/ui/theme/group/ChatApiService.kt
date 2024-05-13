package com.example.mealtoyou.ui.theme.group

import com.example.mealtoyou.ui.theme.diet.DailyDietsResponseDto
import com.example.mealtoyou.ui.theme.diet.Diet
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ChatApiService {
    @GET("api/community-service/communities/users/{userId}")
    suspend fun getUserImage(@Path("userId") userId: Int): Response<List<UserSimpleData>>


    @GET("api/food-service/api/diets?date=2024-05-10")
    suspend fun getUserDiets(@Header("Authorization") authorization: String): Response<DailyDietsResponseDto>
}

