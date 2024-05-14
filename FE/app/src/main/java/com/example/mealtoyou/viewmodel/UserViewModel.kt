package com.example.mealtoyou.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoyou.data.UserHomeResponse
import com.example.mealtoyou.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository = UserRepository) : ViewModel() {

    private var _userHomeResponse by mutableStateOf<UserHomeResponse>(UserHomeResponse())
    val userHomeResponse: UserHomeResponse get() = _userHomeResponse

    fun updateUserHome() {
        Log.d("UserApi", "updateUserHome start")
        viewModelScope.launch {
            try {
                val response = userRepository.getUserHomeSummary()
                if (response.isSuccessful && response.body() != null) {
                    _userHomeResponse = response.body()!!
                    Log.d("UserApi", "updateUserHome success")
                } else {
                    // 요청이 성공적이지 않을 경우의 처리
                    throw Exception("Request failed with error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // 예외 처리
                e.printStackTrace()
            }
        }
    }
}