package com.example.mealtoyou.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoyou.data.BodyResponseData
import com.example.mealtoyou.data.repository.BodyRepository
import com.example.mealtoyou.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.awaitResponse

class BodyViewModel : ViewModel() {
    private val repository = BodyRepository(RetrofitClient.healthInstance)

    var bodyData by mutableStateOf<List<BodyResponseData>?>(null)
        private set

    fun fetchBodyData(day: Int = 1) {
        viewModelScope.launch {
            val response = repository.readBodyData(day).awaitResponse()
            if (response.isSuccessful) {
                bodyData = response.body()
            }
        }
    }
}