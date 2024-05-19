package com.example.mealtoyou.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoyou.data.FoodSearchData
import com.example.mealtoyou.data.repository.FoodSearchRepository
import kotlinx.coroutines.launch

class FoodSearchViewModel: ViewModel() {
    var foodSearchResult = mutableStateOf<List<FoodSearchData>?>(null)
        private set

    fun foodSearch(keyword: String, onResult: (List<FoodSearchData>?) -> Unit = {}) {
        viewModelScope.launch {
            val result = FoodSearchRepository.sendFoodSearchData(keyword)
            foodSearchResult.value = result
            onResult(result) // 콜백을 통해 결과를 반환
        }
    }

    suspend fun foodSearchAsync(keyword: String): MutableList<FoodSearchData>? {
        val result = FoodSearchRepository.sendFoodSearchData(keyword)
        foodSearchResult.value = result
        return result
    }

    // foodSearchResult를 초기화하는 함수
    fun resetFoodSearchResult() {
        foodSearchResult.value = null
    }
}