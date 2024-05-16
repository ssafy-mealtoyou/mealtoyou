package com.example.mealtoyou.api

import com.example.mealtoyou.data.FoodRequestItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DietApiService {
    @GET("/test")
    suspend fun test() : Response<String>

    @POST("/api/food-service/api/diets")
    suspend fun createDiet(@Body foodRequestItemList: List<FoodRequestItem>): Response<Void>
}