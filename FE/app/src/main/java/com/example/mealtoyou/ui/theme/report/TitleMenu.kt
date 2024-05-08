package com.example.mealtoyou.ui.theme.report

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleMenu(titleMenuPointer: Int, onPointerSelected: (Int) -> Unit) {
    Row {
        TitleButton(text = "신체", pointer = 0, selectedPointer = titleMenuPointer) {
            onPointerSelected(0)
        }
        Spacer(modifier = Modifier.width(12.dp))
        TitleButton(text = "운동", pointer = 1, selectedPointer = titleMenuPointer) {
            onPointerSelected(1)
        }
//        Spacer(modifier = Modifier.width(12.dp))
//        TitleButton(text = "수면", pointer = 2, selectedPointer = titleMenuPointer) {
//            onPointerSelected(2)
//        }
    }
    Spacer(modifier = Modifier.height(12.dp))

}

@Composable
fun TitleButton(text: String, pointer: Int, selectedPointer: Int, onClick: () -> Unit) {
    val borderColor by animateColorAsState(
        targetValue = if (pointer == selectedPointer) Color(0xFF6D31ED) else Color(0xFFDEE1E6),
        animationSpec = TweenSpec(durationMillis = 300), label = "테두리 색 변경"
    )
    val textColor by animateColorAsState(
        targetValue = if (pointer == selectedPointer) Color.White else Color(0xFFDEE1E6),
        animationSpec = TweenSpec(durationMillis = 300), label = "텍스트 색 변경"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (pointer == selectedPointer) Color(0xFF6D31ED) else Color.Transparent,
        animationSpec = TweenSpec(durationMillis = 300), label = "백그라운드 색 변경"
    )

    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
            .height(32.dp)
            .padding(start = 21.dp, end = 21.dp)
            .clip(RoundedCornerShape(16.dp))

    ) {
        Text(
            text = text,
            color = textColor,
            lineHeight = 24.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}