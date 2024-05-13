package com.example.mealtoyou.api

import com.example.mealtoyou.data.SupplementResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


interface SupplementApiService {
    @GET("/supplements")
    suspend fun getSupplements(@Header("Authorization") token: String)
    : Response<List<SupplementResponseData>>
}