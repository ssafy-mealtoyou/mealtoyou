package com.example.mealtoyou.ui.theme.main.food.nomal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.R
import com.example.mealtoyou.ui.theme.Pretend

@Composable
fun NutrientInfo() {
    Row {
        Nutrient("탄 33%", R.drawable.tan)
        Spacer(Modifier.width(10.dp))
        Nutrient("단 33%", R.drawable.dan)
        Spacer(Modifier.width(10.dp))
        Nutrient("지 33%", R.drawable.zi)
    }
}

@Composable
fun Nutrient(text: String, iconId: Int) {
    Row {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = "Nutrient Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(2.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Pretend,
            color = Color(0xFF323743)
        )
    }
}