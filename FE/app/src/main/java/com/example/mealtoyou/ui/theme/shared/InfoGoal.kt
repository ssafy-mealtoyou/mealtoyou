package com.example.mealtoyou.ui.theme.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoRow(dailyGoalCalories: Int, dailyGoalSteps: Int, weeklyMinGoal: Int, cntUsers: Int) {
    Row(modifier = Modifier.padding(12.dp)) {
        InfoColumn("일일 목표 소모 칼로리", dailyGoalCalories.toString())
        Spacer(modifier = Modifier.weight(1f))
        InfoColumn("일일 목표 걸음 수", dailyGoalSteps.toString())
        Spacer(modifier = Modifier.weight(1f))
        InfoColumn("주별 최소 인증", weeklyMinGoal.toString())
        Spacer(modifier = Modifier.weight(1f))
        InfoColumn("참가 인원", cntUsers.toString())
    }
}

@Composable
fun InfoColumn(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 10.sp, textAlign = TextAlign.Center, color = Color(0xFF323743))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 26.sp,
            textAlign = TextAlign.Center
            , color = Color(0xFF323743)
        )
    }
}
