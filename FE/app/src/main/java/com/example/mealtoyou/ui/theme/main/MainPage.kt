package com.example.mealtoyou.ui.theme.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mealtoyou.ui.theme.shared.MainBar

@Composable
fun MainPage() {
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
            MainBar("배고파")

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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FoodBox()
                    MyTodayReport()
                    MyStage()
                    MyFeedBack()
                }
            }
        }
    }
}
