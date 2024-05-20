package com.example.mealtoyou.api

import com.example.mealtoyou.data.FoodNutrient
import com.example.mealtoyou.data.FoodRequestItem
import com.example.mealtoyou.data.UserIdData
import com.example.mealtoyou.ui.theme.diet.Diet
import com.example.mealtoyou.ui.theme.diet.DietFood
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface DietApiService {
    @POST("api/ai-service/recommendations")
    suspend fun recommendDiet(@Body userIdData: UserIdData): List<Diet>

    @POST("api/ai-service/asd")
    suspend fun recommendOtherFood(
            @Body foodNutrient : FoodNutrient
    ): List<DietFood>

    @POST("/api/food-service/api/diets")
    suspend fun createDiet(@Body foodRequestItemList: List<FoodRequestItem>): Response<Void>

}