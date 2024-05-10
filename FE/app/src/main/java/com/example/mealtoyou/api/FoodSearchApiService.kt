package com.example.mealtoyou.api

import com.example.mealtoyou.data.FoodSearchData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FoodSearchApiService {
    @GET("/api/foods")
    suspend fun getFoodSearch(
        @Header("Authorization") authorization: String,
        @Query("keyword") keyword: String
    ): Response<List<FoodSearchData>>
}