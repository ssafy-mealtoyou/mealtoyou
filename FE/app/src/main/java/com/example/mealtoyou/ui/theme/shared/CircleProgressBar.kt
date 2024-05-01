package com.example.mealtoyou.ui.theme.shared

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.ui.theme.Pretend

@Composable
fun CircleProgressBar(value:Float, size: Dp) {
    val progress = remember { mutableFloatStateOf(value) }  // 초기 진행 상태는 30%
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
                progress.floatValue
            },
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF6D31ED),
            strokeWidth = 6.dp,
        )
        Text(text = "72KG",
            fontWeight = FontWeight.SemiBold,
            fontFamily = Pretend,
            lineHeight = 30.sp,
            fontSize = 20.sp,
            color = Color(0xFF323743)
        )
    }
}