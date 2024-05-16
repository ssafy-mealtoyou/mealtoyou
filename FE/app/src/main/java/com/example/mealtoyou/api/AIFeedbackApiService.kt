package com.example.mealtoyou.api

import com.example.mealtoyou.data.AIFeedbackResponse
import retrofit2.Response
import retrofit2.http.GET

interface AIFeedbackApiService {
    @GET("api/user-service/users/ai-feedback")
//    @GET("user-service/users/ai-feedback")
    suspend fun getAIFeedback() : Response<AIFeedbackResponse>

}