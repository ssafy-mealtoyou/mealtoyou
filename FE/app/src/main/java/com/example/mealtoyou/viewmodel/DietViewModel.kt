package com.example.mealtoyou.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoyou.data.FoodDetectionResponseItem
import com.example.mealtoyou.data.FoodRequestItem
import com.example.mealtoyou.data.repository.DietImageRepository
import com.example.mealtoyou.data.repository.DietRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class DietViewModel : ViewModel() {

    val dietImageRepository: DietImageRepository = DietImageRepository
    val dietRepository: DietRepository = DietRepository

    // 비동기 작업의 결과를 저장하기 위한 LiveData를 private으로 선언
    private val _analyzeImageResult = MutableLiveData<List<FoodDetectionResponseItem>?>()

    // 외부에서 접근할 수 있도록 LiveData의 getter를 제공
    val analyzeImageResult: LiveData<List<FoodDetectionResponseItem>?> = _analyzeImageResult

    fun analyzeImage(bitmap: Bitmap, onResult: (List<FoodDetectionResponseItem>?) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("AnalyzeImage", "Image analysis started")
                val analyzeImage: Response<List<FoodDetectionResponseItem>> = dietImageRepository.analyzeImage(bitmap, "image")
                if(analyzeImage.isSuccessful) {
                    // 성공적으로 데이터를 받아온 경우
                    Log.d("AnalyzeImage", "Image analysis successful: ${analyzeImage.body()?.size} items found")
                    onResult(analyzeImage.body())
                } else {
                    // 요청 실패 (예: 404 Not Found, 500 Internal Server Error 등)
                    Log.e("AnalyzeImage", "Image analysis request failed: ${analyzeImage.code()} - ${analyzeImage.message()}")
                    onResult(null)
                }
            } catch (e: Exception) {
                // 네트워크 요청 중 예외 발생
                Log.e("AnalyzeImage", "Exception during image analysis: ${e.message}", e)
                onResult(null)
            }
        }
    }

    fun createDiet(foodRequestItemList: List<FoodRequestItem>) {
        viewModelScope.launch {
            try {
                dietRepository.createDiet(foodRequestItemList)
            } catch (e: Exception) {
                // 예외 발생 시 처리
                Log.e("DietViewModel", "createDiet 예외 발생: ${e.message}")
            }
        }
    }
}