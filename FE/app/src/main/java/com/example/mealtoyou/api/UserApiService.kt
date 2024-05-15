package com.example.mealtoyou.api

import com.example.mealtoyou.data.UserHomeResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    @GET("api/user-service/users/home")
    suspend fun getUserHomeSummary() : Response<UserHomeResponse>

}