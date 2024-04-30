package com.example.mealtoyou.ui.theme.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.shared.VerticalProgressBar

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
fun TodayReportBox() {
    val items = listOf(
        0.66f to Pair(Color(0xFF6D31ED), "식단"),
        0.50f to Pair(Color(0xFFFFD317), "영양"),
        0.70f to Pair(Color(0xFF15ABFF), "수분"),
        0.85f to Pair(Color(0xFF62CD14), "활동"),
        0.54f to Pair(Color(0xFFF9623E), "수면")
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
                Spacer(Modifier.weight(1f)) // 레이아웃의 좌우 균형을 위해
                items.forEach { (progress, details) ->
                    ProgressColumn(progress = progress, color = details.first, text = details.second)
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun MyTodayReport() {
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