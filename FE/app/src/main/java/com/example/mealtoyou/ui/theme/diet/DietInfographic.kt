package com.example.mealtoyou.ui.theme.diet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.shared.CircleProgressBarThick
import com.example.mealtoyou.ui.theme.shared.shadowModifier

@Composable
fun DietInfographic(
    dailyCaloriesBurned: Double?,
    dailyFatTaked: Double?,
    dailyCarbohydrateTaked: Double?,
    dailyProteinTaked: Double?
) {
    val value = if (dailyCaloriesBurned == 0.0) {
        0.01f
    } else {
        if (dailyCaloriesBurned != null) {
            (dailyCaloriesBurned / 2000).toFloat()
        } else {
            0.01f
        }

    }
    Column(Modifier.padding(start = 20.dp, end = 20.dp)) {
        Box(
            modifier = shadowModifier()
                .fillMaxWidth()
                .height(164.dp)
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (dailyCaloriesBurned != null) {
                    CircleProgressBarThick(
                        size = 140.dp, value = value,
                        dailyCaloriesBurned = dailyCaloriesBurned
                    )
                }
                Text(text = "1234")
                Column {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "주요영양소", color = Color(0xFF9095A1), fontFamily = Pretend)
                    NutrientInfoRow(
                        "탄수화물",
                        Color(0xFF62CD14),
                        dailyCarbohydrateTaked.toString(),
                        "/150g"
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    NutrientInfoRow("단백질", Color(0xFFFFD317), dailyProteinTaked.toString(), "/150g")
                    Spacer(modifier = Modifier.weight(1f))
                    NutrientInfoRow("지방", Color(0xFFF9623E), dailyFatTaked.toString(), "/33g")
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun NutrientInfoRow(
    nutrientName: String,
    color: Color,
    currentAmount: String,
    totalAmount: String
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
                    .padding(10.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = nutrientName,
                color = Color(0xff171A1F),
                fontFamily = Pretend,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Text(
                    text = currentAmount.substringBefore("."),
                    color = Color(0xff171A1F),
                    fontFamily = Pretend
                )
                Text(text = totalAmount, color = Color(0xff9095A1), fontFamily = Pretend)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        CustomProgressBar(color, currentAmount.substringBefore(".") + ".0", totalAmount)
    }
}

@Composable
fun CustomProgressBar(color: Color, currentAmount: String, totalAmount: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Log.d("data", currentAmount.toFloat().toString())
        val progress =
            if (currentAmount == "0.0") {
                0.01f
            } else {
                val ratio =
                    currentAmount.toFloat() / totalAmount.substring(1, totalAmount.length - 1)
                        .toFloat()
                if (ratio > 1.0f) {
                    1.0f
                } else {
                    ratio
                }
            }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Color.White),
            color = color, // 진행된 부분의 색상
            trackColor = Color(0xFFF3F4F6), // 남은 부분의 색상
            strokeCap = StrokeCap.Round, // 선의 끝모양을 둥글게
        )
    }
}