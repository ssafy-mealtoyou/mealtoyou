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

    fun foodSearch(keyword: String) {
        viewModelScope.launch {
            foodSearchResult.value = FoodSearchRepository.sendFoodSearchData(keyword)
        }
    }
}