package com.example.mealtoyou.ui.theme.diet

import CalendarScreen
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoyou.MainApplication
import com.example.mealtoyou.retrofit.RetrofitClient
import com.example.mealtoyou.ui.theme.shared.DietBox
import com.example.mealtoyou.ui.theme.shared.MainBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class DietViewModel : ViewModel() {
    private val diet2ApiService = RetrofitClient.diet2Instance
    private val _dailyDiets = MutableStateFlow<DailyDietsResponseDto?>(null)
    val dailyDiets: StateFlow<DailyDietsResponseDto?> = _dailyDiets

    fun fetchData(authorization: String, date: String) {
        viewModelScope.launch {
            try {
                val response = diet2ApiService.getDietList(date)
                if (response.isSuccessful) {
                    val data = response.body()
                    _dailyDiets.value = data
                    Log.d("_dailyDiets", data.toString())
                } else {
                    val errorMessage =
                        "Error: ${response.code()} ${response.message()}"
                    Log.e("error", errorMessage)
                }
            } catch (e: Exception) {
                Log.e("error", "2번 에러입니다.")
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DietPage(viewModel: DietViewModel) {
    val dailyDiets by viewModel.dailyDiets.collectAsState()
    val today = LocalDate.now().toString()
    LaunchedEffect(viewModel) {
        viewModel.fetchData(MainApplication.prefs.getValue("accessToken"), today)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            dailyDiets?.let { data ->
                MainBar(text = "식단", infoImg = true)
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxHeight()
                ) {
                    CalendarScreen(viewModel) // viewModel 전달
                    Column(Modifier.padding(start = 20.dp, end = 20.dp)) {
                        val count: Int = data.diets.size
                        val pagerState = rememberPagerState(
                            pageCount = { count }  // 총 페이지 수
                        )
                        if(data.diets.isNotEmpty()) {
                            DietBox(data.diets, pagerState, false)
                        } else {
                            DietBox(null, null, false)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DietInfographic(
                        data.dailyCaloriesBurned,
                        data.dailyFatTaked,
                        data.dailyCarbohydrateTaked,
                        data.dailyProteinTaked
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

    }
}