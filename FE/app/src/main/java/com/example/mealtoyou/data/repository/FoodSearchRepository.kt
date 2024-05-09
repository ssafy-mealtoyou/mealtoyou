package com.example.mealtoyou.data.repository

import android.util.Log
import com.example.mealtoyou.data.FoodSearchData
import com.example.mealtoyou.retrofit.RetrofitClient

object FoodSearchRepository {
    private val foodSearchResult: MutableList<FoodSearchData> = mutableListOf()

    suspend fun sendFoodSearchData(keyword: String): MutableList<FoodSearchData>? {
        foodSearchResult.clear()

        val response = RetrofitClient.foodSearchInstance.getFoodSearch(
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxZnFmbFBsZjZDd0gySzR1SlVlUm9nPT0iLCJpYXQiOjE3MTUxMzE3MDgsImV4cCI6MTcxNTIxODEwOH0.2-vKVWbwLaiIC6spqzI_MSpCMy7ZdL7GF30CWxNbXh0",
            keyword
        )
        if (response.isSuccessful) {
            Log.d("음식 데이터 결과", response.body().toString())
            response.body()?.let{ responseBody->
                foodSearchResult.addAll(responseBody)
            }
        }
        return foodSearchResult;
    }
}