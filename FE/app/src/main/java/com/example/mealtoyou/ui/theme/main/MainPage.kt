package com.example.mealtoyou.ui.theme.main

import SupplementViewModel
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mealtoyou.MainApplication
import com.example.mealtoyou.ui.theme.main.feedback.MyFeedBack
import com.example.mealtoyou.ui.theme.main.food.DateLabel
import com.example.mealtoyou.ui.theme.main.report.MyTodayReport
import com.example.mealtoyou.ui.theme.main.stage.MyStage
import com.example.mealtoyou.ui.theme.shared.DietBox
import com.example.mealtoyou.ui.theme.shared.MainBar
import com.example.mealtoyou.viewmodel.UserViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPage(supplementViewModel: SupplementViewModel, userViewModel: UserViewModel = viewModel()) {
    val viewModel: SupplementViewModel = viewModel() // Assuming
    val dietsState = supplementViewModel.diets.collectAsState()
    LaunchedEffect(true) {
        Log.d("MainPage", "LaunchedEffect called4")
        try {
            userViewModel.updateUserHome()
            supplementViewModel.loadDiets(MainApplication.prefs.getValue("userId").toInt())
        } catch (e: Exception) {
            Log.d("MainPage", "LaunchedEffect 오류 발생")
        }

    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        // 전체 페이지를 담는 Column
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 스크롤되지 않고 항상 상단에 고정될 MainBarIcon
            MainBar("", true)

            // 스크롤 가능한 부분을 Box와 함께 생성
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()) {
                // 스크롤 상태를 기억
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState), // 스크롤 가능하게 만드는 모디파이어 추가
                    verticalArrangement = Arrangement.Top,
                ) {
                    Log.d("식단추천",dietsState.value.toString());
                    DateLabel()
                    if (dietsState.value.size != 0) {
                        val pagerState = rememberPagerState(pageCount = { dietsState.value.size })
                        Column(Modifier.padding(start = 20.dp, end = 20.dp)) {
                            DietBox(dietsState.value, pagerState, true)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    MyTodayReport()
                    MyStage(supplementViewModel)
                    MyFeedBack()

                }
            }
        }
    }
}

