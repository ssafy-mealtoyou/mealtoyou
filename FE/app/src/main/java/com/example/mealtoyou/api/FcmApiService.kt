package com.example.mealtoyou.api

import com.example.mealtoyou.data.FcmData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmApiService {
    @POST("users/fcm")
    fun postFcmData(@Header("Authorization") authorization: String, @Body fcmData: FcmData): Call<Void>

}