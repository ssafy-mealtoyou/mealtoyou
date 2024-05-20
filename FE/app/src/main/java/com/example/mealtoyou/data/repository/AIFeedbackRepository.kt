package com.example.mealtoyou.data.repository

import com.example.mealtoyou.api.AIFeedbackApiService
import com.example.mealtoyou.data.AIFeedbackResponse
import com.example.mealtoyou.retrofit.RetrofitClient

object AIFeedbackRepository {
    val aiFeedbackApiService: AIFeedbackApiService = RetrofitClient.aiFeedbackInstance

    suspend fun getAIFeedback(): AIFeedbackResponse {
        return try {
            // Retrofit의 비동기 호출을 수행합니다.
            val response = aiFeedbackApiService.getAIFeedback()

            // 응답의 성공 여부를 확인합니다.
            if (response.isSuccessful && response.body() != null) {
                // 성공적으로 데이터를 받아왔다면, 해당 데이터를 반환합니다.
                response.body()!!
            } else {
                // 응답이 실패했다면, 사용자 정의 예외를 던집니다.
                throw Exception("API 호출 실패: ${response.code()}")
            }
        } catch (e: Exception) {
            // 네트워크 호출 중 발생한 예외를 처리합니다.
            throw Exception("네트워크 호출 중 오류 발생: ${e.message}")
        }
    }

}