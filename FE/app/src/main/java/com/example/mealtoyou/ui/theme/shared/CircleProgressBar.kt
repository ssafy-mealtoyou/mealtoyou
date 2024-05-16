package com.example.mealtoyou.ui.theme.shared

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.ui.theme.Pretend

@Composable
fun CircleProgressBar(value:Float, size: Dp, text: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        CircularProgressIndicator(
            progress = {
                1.0f
            },
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFD3C1FA),
            strokeWidth = 6.dp,
        )
        CircularProgressIndicator(
            progress = {
                value
            },
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF6D31ED),
            strokeWidth = 6.dp,
        )
        Text(text = text,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Pretend,
            lineHeight = 30.sp,
            fontSize = 20.sp,
            color = Color(0xFF323743)
        )
    }
}

@Composable
fun CircleProgressBarThick(value:Float, size: Dp, dailyCaloriesBurned: Double) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        CircularProgressIndicator(
            progress = { 1.0f },
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFD3C1FA),
            strokeWidth = 14.dp,
        )
        CircularProgressIndicator(
            progress = { value },
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF6D31ED),
            strokeWidth = 14.dp,
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) { // 가로 방향으로 가운데 정렬
            Text(
                text = "남은 칼로리",
                fontWeight = FontWeight.SemiBold,
                fontFamily = Pretend,
                lineHeight = 20.sp,
                fontSize = 12.sp,
                color = Color(0xFF9095A1),
                textAlign = TextAlign.Center // 텍스트 내에서 가운데 정렬
            )
            Text(
                text = (2000 - dailyCaloriesBurned).toInt().toString() + "kcal",
                fontWeight = FontWeight.Bold,
                fontFamily = Pretend,
                lineHeight = 30.sp,
                fontSize = 20.sp,
                color = Color(0xFF323743),
                textAlign = TextAlign.Center // 텍스트 내에서 가운데 정렬
            )
        }
    }
}