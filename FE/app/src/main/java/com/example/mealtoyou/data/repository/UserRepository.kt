package com.example.mealtoyou.data.repository

import com.example.mealtoyou.api.UserApiService
import com.example.mealtoyou.data.UserHomeResponse
import com.example.mealtoyou.retrofit.RetrofitClient
import retrofit2.Response

object UserRepository {
    private val userApiService: UserApiService = RetrofitClient.userInstance

    suspend fun getUserHomeSummary() : Response<UserHomeResponse> {
        return userApiService.getUserHomeSummary()
    }

}