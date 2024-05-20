package com.example.mealtoyou.ui.theme.diet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.ui.theme.shared.CircleProgressBarThick
import com.example.mealtoyou.ui.theme.shared.shadowModifier

@Composable
fun DietTotalCal() {
    Column(Modifier.padding(start = 20.dp, end = 20.dp)) {
        Box (modifier = shadowModifier()
            .fillMaxWidth()
            .background(Color(0xFFF3F4F6))
            .height(50.dp)
            .padding(12.dp)) {
            Text(text = "1,736 kcal", color = Color(0xFF171A1F), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}