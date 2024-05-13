package com.example.mealtoyou.data.repository

import com.example.mealtoyou.data.FoodRequestItem
import com.example.mealtoyou.retrofit.RetrofitClient
import retrofit2.Response

object DietRepository {
    private val dietApiService by lazy {
        RetrofitClient.dietInstance
    }

    suspend fun createDiet(foodRequestItemList: List<FoodRequestItem>): Response<Void> {
        return dietApiService.createDiet(foodRequestItemList)
    }

}