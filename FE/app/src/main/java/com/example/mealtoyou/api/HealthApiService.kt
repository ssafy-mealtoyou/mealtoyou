package com.example.mealtoyou.api

import com.example.mealtoyou.data.ExerciseData
import com.example.mealtoyou.data.HealthData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.Date

interface HealthApiService {
    @POST("api/health-service/health/body-fat")
    fun postHealthData(@Header("Authorization") authorization: String,@Body healthData: HealthData): Call<Void>

    @POST("api/health-service/health/exercise")
    suspend fun postExerciseData(@Header("Authorization") authorization: String,@Body exerciseData: ExerciseData) : Response<Void>

}

