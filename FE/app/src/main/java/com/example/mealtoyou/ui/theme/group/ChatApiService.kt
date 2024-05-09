package com.example.mealtoyou.ui.theme.group

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatApiService {
    @GET("community-service/communities/users/{userId}")
    suspend fun getUserImage(@Path("userId") userId: Int): Response<List<UserSimpleData>>

}

