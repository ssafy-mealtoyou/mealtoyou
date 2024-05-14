package com.example.mealtoyou.api

import com.example.mealtoyou.data.SupplementRequestData
import com.example.mealtoyou.data.SupplementResponseData
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface SupplementApiService {
//    @GET("api/supplement-service/supplements")
    @GET("supplements")
    suspend fun getSupplements()
    : Response<List<SupplementResponseData>>

//    @POST("api/supplement-service/supplements")
    @POST("supplements")
    suspend fun registerSupplements(
    @Body supplementDataList: List<SupplementRequestData>)
    : Response<String>
}