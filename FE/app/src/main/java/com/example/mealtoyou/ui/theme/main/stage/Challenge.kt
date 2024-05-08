package com.example.mealtoyou.ui.theme.main.stage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.R
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.shared.CircleProgressBar
import com.example.mealtoyou.ui.theme.shared.VerticalProgressBar

@Composable
fun WeightProgress(value: Float, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircleProgressBar(value, 80.dp)
        Spacer(modifier = Modifier.height(9.dp))
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Pretend,
            lineHeight = 20.sp,
            fontSize = 12.sp,
            color = Color(0xFF323743)
        )
    }
}

@Composable
fun Challenge(modifier: Modifier, color: Color, setupAble: Boolean) {
    Box(
        modifier = modifier
            .height(165.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color(0xFF171A1F),
                spotColor = Color(0xFF171A1F)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(color)
    ) {
        Column(Modifier.padding(10.dp)) {
            Row {
                Text(
                    text = "목표 달성치",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretend,
                    lineHeight = 20.sp,
                    fontSize = 12.sp,
                    color = Color(0xFF9095A1)
                )
                Spacer(modifier = Modifier.weight(1f))
                if (setupAble) {
                    Image(
                        painter = painterResource(id = R.drawable.gear),
                        contentDescription = "gear",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Row {
                WeightProgress(value = 0.7f, "몸무게")
                Spacer(modifier = Modifier.width(24.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VerticalProgressBar(
                        progress = 0.7f,
                        color = Color(0xFF6D31ED),
                        width = 29.dp,
                        height = 80.dp
                    )
                    Spacer(modifier = Modifier.height(9.dp))
                    Text(
                        text = "D-12",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Pretend,
                        lineHeight = 20.sp,
                        fontSize = 12.sp,
                        color = Color(0xFF323743)
                    )
                }
            }

        }
    }
}
