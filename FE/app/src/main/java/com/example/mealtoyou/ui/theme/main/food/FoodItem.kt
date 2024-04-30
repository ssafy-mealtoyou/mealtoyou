package com.example.mealtoyou.ui.theme.main.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.R
import com.example.mealtoyou.ui.theme.Pretend

@Composable
fun FoodItems(showTemp: MutableState<Boolean>, selectedItem: MutableState<String>) {
    val koreanFoods = listOf("김치", "불고기", "비빔밥", "된장찌개", "김밥", "떡볶이")

    Column {

        Row(modifier = Modifier.padding(top = 12.dp)) {
            repeat(3) { index ->
                FoodItem(koreanFoods[index], showTemp, selectedItem)
                if (index < 2) Spacer(Modifier.weight(1f))
            }
        }

        Row(modifier = Modifier.padding(top = 12.dp)) {
            repeat(3) { index ->
                FoodItem(koreanFoods[index + 3], showTemp, selectedItem)
                if (index < 2) Spacer(Modifier.weight(1f))
            }
        }

        Row {
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { showTemp.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D31ED)),
                shape = RoundedCornerShape(12),
                modifier = Modifier
                    .width(100.dp)
                    .height(55.dp)
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = "등록하기",
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun FoodItem(
    itemName: String,
    showTemp: MutableState<Boolean>,
    selectedItem: MutableState<String>
) {
    Box(
        modifier = Modifier
            .height(120.dp)
            .width(104.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                selectedItem.value = itemName
                showTemp.value = true
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.tm),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(Modifier.padding(6.dp)) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp)) // 흰색 배경과 둥근 모서리 적용
                    .padding(horizontal = 6.dp, vertical = 2.dp) // 텍스트 주변의 패딩
            ) {
                Text(
                    text = itemName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Pretend,
                    lineHeight = 17.sp,
                    color = Color(0xFF171A1F)
                )
            }
            Spacer(Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .background(
                        Color.DarkGray,
                        shape = RoundedCornerShape(8.dp)
                    ) // 흰색 배경과 둥근 모서리 적용
                    .padding(horizontal = 5.dp, vertical = 1.dp) // 텍스트 주변의 패딩
            ) {
                Text(
                    text = "215kcal",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Pretend,
                    color = Color.White
                )
            }
        }
    }
}