package com.example.mealtoyou.ui.theme.main.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.shared.VerticalProgressBar
import com.example.mealtoyou.viewmodel.UserViewModel

@Composable
fun ProgressColumn(progress: Float, color: Color, text: String) {
    Column {
        VerticalProgressBar(progress = progress, color = color, 40.dp, 80.dp)
        Spacer(Modifier.height(4.dp))
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Pretend,
            fontSize = 12.sp,
            color = Color(0xFF323743),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun TodayReportBox(userViewModel: UserViewModel = viewModel()) {
    val userHomeResponse = userViewModel.userHomeResponse
    val items = listOf(
        userHomeResponse?.daySummary?.dietPer to Pair(Color(0xFF6D31ED), "식단"),
        userHomeResponse?.daySummary?.caloriesPer to Pair(Color(0xFFFFD317), "영양"),
        userHomeResponse?.daySummary?.activityPer to Pair(Color(0xFF62CD14), "활동"),
    )

    val descriptions = listOf(
        "식단: 하루 3끼 섭취 비율",
        "영양: 하루 칼로리 섭취 비율",
        "활동: 하루 걸음 수 충족 비율",
    )

    Box(
        modifier = Modifier
            .height(165.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color(0xFF171A1F),
                spotColor = Color(0xFF171A1F)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "내 하루 요약",
                fontSize = 12.sp,
                fontFamily = Pretend,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF9095A1)
            )
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                items.forEachIndexed { index, (progress, details) ->
                    Column {
                        if (progress != null) {
                            ProgressColumn(progress = (progress / 100f), color = details.first, text = details.second)
                        } else {
                            ProgressColumn(progress = 0f, color = details.first, text = details.second)
                        }
                    }
                    // 아이템 간에 16.dp의 여백 추가
                    if (index < items.size - 1) {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
                Spacer(modifier = Modifier.width(32.dp))
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Text 컴포저블들 사이에 동일한 여백 설정
                ) {
                    descriptions.forEach { text ->
                        Text(
                            text = text,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Pretend,
                            fontSize = 12.sp,
                            color = Color(0xFF323743),
//                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyTodayReport(
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
            .padding(end = 20.dp)
            .padding(bottom = 20.dp)
    ) {
        TodayReportBox()
    }
}