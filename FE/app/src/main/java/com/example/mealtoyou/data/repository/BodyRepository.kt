package com.example.mealtoyou.data.repository

import com.example.mealtoyou.api.HealthApiService
import com.example.mealtoyou.data.BodyResponseData
import retrofit2.Call

class BodyRepository(private val apiService: HealthApiService) {
    fun readBodyData(day: Int = 1): Call<List<BodyResponseData>> {
        return apiService.readBodyData(day)
    }
}