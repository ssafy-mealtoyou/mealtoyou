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

@Composable
fun Drug(drug: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = drug),
            contentDescription = "drug Icon",
            modifier = Modifier.size(45.dp)
        )
        Text(
            text = "비타민",
            color = Color(0xFF323743),
            fontWeight = FontWeight.SemiBold,
            fontFamily = Pretend,
            lineHeight = 20.sp,
            fontSize = 12.sp,
        )
    }
}

@Composable
fun DrugInfo(modifier: Modifier) {
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
            .background(Color.White)
    ) {
        Column(Modifier.padding(10.dp)) {
            Text(
                text = "영양제",
                fontWeight = FontWeight.SemiBold,
                fontFamily = Pretend,
                lineHeight = 20.sp,
                color = Color(0xFF9095A1),
                fontSize = 12.sp,
            )
            Text(
                text = "66% 완료",
                fontWeight = FontWeight.SemiBold,
                fontFamily = Pretend,
                lineHeight = 26.sp,
                fontSize = 16.sp,
                color = Color(0xFF323743)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row() {
                Spacer(modifier = Modifier.weight(1f))
                Drug(drug = R.drawable.used_drug)
                Spacer(modifier = Modifier.weight(1f))
                Drug(drug = R.drawable.used_drug)
                Spacer(modifier = Modifier.weight(1f))
                Drug(drug = R.drawable.unused_drug)
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}