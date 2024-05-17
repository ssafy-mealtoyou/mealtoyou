package com.example.mealtoyou.api

import com.example.mealtoyou.ui.theme.diet.DailyDietsResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Diet2ApiService {
    @GET("api/food-service/api/diets")
    suspend fun getDietList(
        @Query("date") date: String
    ): Response<DailyDietsResponseDto>
}