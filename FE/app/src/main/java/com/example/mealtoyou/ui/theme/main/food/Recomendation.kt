package com.example.mealtoyou.ui.theme.main.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun RecommendationBox() {
    Box(
        modifier = Modifier
            .height(450.dp)
            .fillMaxWidth()
            .padding(bottom = 20.dp, top = 8.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color(0xFF171A1F),
                spotColor = Color(0xFF171A1F)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        RecommendationContent()
    }
}

@Composable
fun RecommendationContent() {
    val showTemp = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf("") }

    if (showTemp.value) {
        Temp(selectedItem.value, showTemp)
    } else {
        NormalContent(showTemp, selectedItem)
    }
}

@Composable
fun RecommendationHeader() {
    Row {
        Text(
            text = "추천식단",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Pretend,
            color = Color(0xFF9095A1),
            modifier = Modifier.padding(top = 5.dp)
        )
        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.star),
            contentDescription = "Google Icon",
            modifier = Modifier.size(28.dp)
        )
    }
}