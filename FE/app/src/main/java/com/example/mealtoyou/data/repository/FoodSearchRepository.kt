package com.example.mealtoyou.data.repository

import android.util.Log
import com.example.mealtoyou.data.FoodSearchData
import com.example.mealtoyou.retrofit.RetrofitClient

object FoodSearchRepository {
    private val foodSearchResult: MutableList<FoodSearchData> = mutableListOf()

    suspend fun sendFoodSearchData(keyword: String): MutableList<FoodSearchData>? {
        foodSearchResult.clear()

        val response = RetrofitClient.foodSearchInstance.getFoodSearch(
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0dVNFTU5WWlo4NlJvODFxRDA5K21nPT0iLCJpYXQiOjE3MTUyOTk5MDcsImV4cCI6MTcxNTM4NjMwN30.0vgc7AXURd6fT4-MLiBXU8VPJGTPeZghYXyrMd4Unyg",
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