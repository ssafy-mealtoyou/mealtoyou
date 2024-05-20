package com.example.mealtoyou.ui.theme.group

import com.example.mealtoyou.ui.theme.diet.DailyDietsResponseDto
import com.example.mealtoyou.ui.theme.diet.Diet
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface ChatApiService {
    @GET("api/community-service/communities/users/{userId}")
    suspend fun getUserImage(@Path("userId") userId: Int): Response<List<UserSimpleData>>


    @GET("api/food-service/api/diets")
    suspend fun getUserDiets(
        @Header("Authorization") authorization: String,
        @Query("date") date: String = getCurrentDate()
    ): Response<DailyDietsResponseDto>

    companion object {
        fun getCurrentDate(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(Date())
        }
    }
}

