package com.example.mealtoyou.data.repository

import com.example.mealtoyou.api.HealthApiService
import com.example.mealtoyou.data.BodyResponseData
import com.example.mealtoyou.data.ExerciseData
import retrofit2.Call

class ExerciseRepository(private val apiService : HealthApiService) {
    fun readExerciseData(day: Int = 1): Call<List<ExerciseData>> {
        return apiService.readExerciseData(day)
    }
}