package com.example.mealtoyou.api

import com.example.mealtoyou.data.FoodSearchData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodSearchApiService {
    @GET("/api/food-service/api/foods")
    suspend fun getFoodSearch(
        @Query("keyword") keyword: String
    ): Response<List<FoodSearchData>>
}