package com.example.mealtoyou.api

import com.example.mealtoyou.data.FoodDetectionResponseItem
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DietImageApiService {
    @Multipart
    @POST("/api/foods/analyze-image")
    suspend fun analyzeImage(@Part image: MultipartBody.Part): Response<List<FoodDetectionResponseItem>>
}