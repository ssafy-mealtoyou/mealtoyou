package com.example.mealtoyou.api

import com.example.mealtoyou.data.UserHealthInfoUpdateData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT

interface User2ApiService {

    @PUT("api/user-service/users/inbody")
    suspend fun postHealthInfo(
        @Header("Authorization") authorization: String,
        @Body requestDto: UserHealthInfoUpdateData
    ): Response<Void>

}