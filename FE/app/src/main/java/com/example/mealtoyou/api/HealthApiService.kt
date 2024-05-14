package com.example.mealtoyou.api

import com.example.mealtoyou.data.BodyResponseData
import com.example.mealtoyou.data.ExerciseData
import com.example.mealtoyou.data.HealthData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Date

interface HealthApiService {
    @POST("api/health-service/health/body-fat")
    fun postHealthData(@Body healthData: HealthData): Call<Void>

    @POST("api/health-service/health/exercise")
    suspend fun postExerciseData(@Body exerciseData: ExerciseData) : Response<Void>

    @GET("health/body-fat")
    fun readBodyData(
        @Query("day") day: Int = 1
    ): Call<List<BodyResponseData>>

    @GET("health/exercise")
    fun readExerciseData(@Query("day") day: Int = 1)
    : Call<List<ExerciseData>>
}

