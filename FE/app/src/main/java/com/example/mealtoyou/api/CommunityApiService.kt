package com.example.mealtoyou.api

import com.example.mealtoyou.data.CommunityData
import com.example.mealtoyou.data.CreateCommunityData
import com.example.mealtoyou.data.UserCommunityData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityApiService {

    // 커뮤니티 생성
    @POST("api/community-service/communities")
    fun createCommunity(
        @Header("Authorization") authorization: String,
        @Body createCommunityData: CreateCommunityData
    ): Call<Void>

    // 커뮤니티 조회
    @GET("api/community-service/communities/my")
    suspend fun getUserCommunityInfo(
    ): Response<UserCommunityData>

    // 전체 그룹 조회
    @GET("api/community-service/communities")
    suspend fun getCommunityList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<List<CommunityData>>

    // 일일 목표 인증
    @POST("api/community-service/goals")
    fun dailyGoalCheck(
        @Header("Authorization") authorization: String,
        @Query("steps") steps: Int,
        @Query("caloriesBurned") caloriesBurned: Int
    ): Call<String>

    // 커뮤니티 가입
    @POST("api/community-service/communities/{communityId}")
    suspend fun joinCommunity(
        @Header("Authorization") authorization: String,
        @Path("communityId") communityId: Long
    ): Response<String>

    // 커뮤니티 가입 확인
    @GET("api/community-service/communities/check")
    suspend fun checkStatus(): Response<String>

}