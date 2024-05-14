package com.example.mealtoyou.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoyou.data.BodyResponseData
import com.example.mealtoyou.data.ExerciseData
import com.example.mealtoyou.data.repository.BodyRepository
import com.example.mealtoyou.data.repository.ExerciseRepository
import com.example.mealtoyou.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class ExerciseViewModel: ViewModel() {
    private val repository = ExerciseRepository(RetrofitClient.healthInstance)

    var exerciseData by mutableStateOf<List<ExerciseData>?>(null)
        private set

    fun fetchExerciseData(day: Int = 1) {
        viewModelScope.launch {
            val response = repository.readExerciseData(day).awaitResponse()
            if (response.isSuccessful) {
                exerciseData = response.body()
            }
        }
    }
}
