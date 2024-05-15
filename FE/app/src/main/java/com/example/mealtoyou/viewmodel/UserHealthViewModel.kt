package com.example.mealtoyou.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoyou.data.model.response.UserHealthResDto
import com.example.mealtoyou.retrofit.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserHealthViewModel : ViewModel() {
    val _userHealthResult = MutableStateFlow<UserHealthResDto?>(
        UserHealthResDto(
            nickname = "",
            imageUrl = "",
            inbodyBodyFat = 0.0,
            inbodyBoneMuscle =0.0,
            intermittentYn =false,
            intermittentStartTime ="00:00:00",
            intermittentEndTime ="00:00:00",
            weight =0.0,
            weightLastMonth =0.0,
            weightThisYear =0.0,
            goalDate ="01-01",
            goalWeight =0.0
        )
    )

    suspend fun refreshUserHealth() {
        viewModelScope.launch {
            val result = readUserHealth()
            if (result != null) {
                _userHealthResult.value = result
                Log.d("새로고침",result.toString())
            }
        }
    }

    init {
        viewModelScope.launch {
            Log.d("뷰모델1", "here")
            val result = readUserHealth()
            Log.d("뷰모델2", result.toString())
            if (result != null) {
                _userHealthResult.value = result
                Log.d("UserHealthviewModel", result.toString())
            }
        }
    }

    private suspend fun readUserHealth(): UserHealthResDto? {
        val response = RetrofitClient.userInstance.getUserHealth()
        Log.d("리스폰스", response.toString())
        return if (response.isSuccessful) {
            Log.d("리스폰스", response.body().toString())
            response.body()
        } else {
            null
        }
    }
}