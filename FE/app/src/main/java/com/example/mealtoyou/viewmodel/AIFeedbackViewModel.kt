package com.example.mealtoyou.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoyou.data.AIFeedbackResponse
import com.example.mealtoyou.data.repository.AIFeedbackRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class AIFeedbackViewModel(val aiFeedbackRepository: AIFeedbackRepository = AIFeedbackRepository) : ViewModel() {

    private var _feedbackResponse by mutableStateOf<AIFeedbackResponse>(AIFeedbackResponse())
    val feedbackResponse: AIFeedbackResponse get() = _feedbackResponse

    // 마지막 업데이트 시간을 저장할 변수입니다.
    private var lastUpdateTime: LocalDateTime? = null

    fun updateAIFeedBack() {
        viewModelScope.launch {
            // 현재 시간과 마지막 업데이트 시간의 차이가 30분 이상이거나 응답이 빈 문자열인 경우에만 업데이트를 수행합니다.
            val shouldUpdate = lastUpdateTime == null ||
                    ChronoUnit.MINUTES.between(lastUpdateTime, LocalDateTime.now()) >= 30 ||
                    _feedbackResponse.content.isEmpty()

            if (shouldUpdate) {
                try {
                    val response = aiFeedbackRepository.getAIFeedback()
                    // 빈 문자열 검사를 추가합니다.
                    if (response.content.isNotEmpty()) {
                        _feedbackResponse = response
                        // 업데이트 성공 시 마지막 업데이트 시간을 현재 시간으로 설정합니다.
                        lastUpdateTime = LocalDateTime.now()
                    } else {
                        // 빈 문자열인 경우 로그를 남깁니다.
                        Log.e("AIFeedbackViewModel", "Received empty feedback")
                    }
                } catch (e: Exception) {
                    Log.e("AIFeedbackViewModel", "Failed to fetch AI feedback: ${e.message}")
                }
            } else {
                Log.d("AIFeedbackViewModel", "Update not required or waiting for 30 minutes interval.")
            }
        }
    }

}