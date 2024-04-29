package com.example.mealtoyou.ui.theme.shared

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerticalProgressBar(progress: Float, color: Color, width: Dp, height: Dp) {  // progress는 0.0 (0%)에서 1.0 (100%) 사이의 값
    Box(
        modifier = Modifier
            .height(height)
            .width(width)
            .background(Color(0xFFF3F4F6), shape = RoundedCornerShape(8.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val drawHeight = size.height * progress
            if (drawHeight > 0) {  // 높이가 0 이상일 때만 그림
                drawRoundRect(
                    color = color,
                    topLeft = Offset(x = 0f, y = size.height - drawHeight),
                    size = Size(width = size.width, height = drawHeight),
                    cornerRadius = CornerRadius(x = 10f, y = 10f)  // 모서리 반경 적용
                )
            }
        }
    }
}
