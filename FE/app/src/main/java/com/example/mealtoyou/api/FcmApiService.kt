package com.example.mealtoyou.api

import com.example.mealtoyou.data.FcmData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface FcmApiService {
    @PUT("users/fcm")
    fun postFcmData(@Body fcmData: FcmData): Call<Void>

}