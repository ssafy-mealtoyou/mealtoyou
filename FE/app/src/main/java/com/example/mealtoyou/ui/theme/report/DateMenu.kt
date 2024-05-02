package com.example.mealtoyou.ui.theme.report

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DateMenu(selectedPointer: Int, onPointerSelected: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .background(color = Color(0xFFF3F4F6), shape = RoundedCornerShape(6.dp))
            .height(34.dp)
            .padding(2.dp)
            .fillMaxWidth()
    ) {
        Row {
            DateButton(
                text = "최근 7일",
                pointer = 0,
                selectedPointer = selectedPointer,
                Modifier.weight(1f)
            ) {
                onPointerSelected(0)
            }
            Spacer(modifier = Modifier.width(4.dp))
            DateButton(
                text = "최근 30일",
                pointer = 1,
                selectedPointer = selectedPointer,
                Modifier.weight(1f)
            ) {
                onPointerSelected(1)
            }
            Spacer(modifier = Modifier.width(4.dp))
            DateButton(
                text = "전체 기간",
                pointer = 2,
                selectedPointer = selectedPointer,
                Modifier.weight(1f)
            ) {
                onPointerSelected(2)
            }
        }
    }
}

@Composable
fun DateButton(
    text: String,
    pointer: Int,
    selectedPointer: Int,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (pointer == selectedPointer) Color.White else Color.Transparent

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(6.dp))
            .height(32.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(0xFF171A1F),
            lineHeight = 20.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}