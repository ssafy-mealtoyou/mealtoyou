package com.example.mealtoyou.ui.theme.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow

import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.R
import com.example.mealtoyou.ui.theme.Pretend

@Composable
fun FoodBox() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        DateLabel("TUES 11 JUL")
        RecommendationBox()
    }
}

@Composable
fun DateLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Pretend,
        color = Color(0xff565d6d),
    )
}

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
    Column(Modifier.padding(12.dp)) {
        RecommendationHeader()
        CalorieInfo("862kcal")
        NutrientInfo()
        FoodItems()
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

@Composable
fun CalorieInfo(calories: String) {
    Text(
        text = calories,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Pretend,
        color = Color(0xFF171A1F)
    )
}

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

@Composable
fun FoodItems() {
    Column {
        // 첫 번째 행의 음식 아이템
        Row(modifier = Modifier.padding(top = 12.dp)) {
            repeat(3) {
                FoodItem()
                if (it < 2) Spacer(Modifier.weight(1f))
            }
        }
        // 두 번째 행의 음식 아이템
        Row(modifier = Modifier.padding(top = 12.dp)) {
            repeat(3) {
                FoodItem()
                if (it < 2) Spacer(Modifier.width(7.dp))
            }
        }
        Row {
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { /* TODO: 여기에 버튼 클릭 이벤트 처리 코드를 추가하세요 */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6D31ED), // 보라색 지정
                ),
                shape = RoundedCornerShape(12), // 버튼 모서리를 둥글게 처리
                modifier = Modifier
                    .width(100.dp)
                    .height(55.dp)
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = "등록하기",
                    color = Color.White, // 텍스트 색상을 흰색으로 설정
                )
            }
        }
    }
}

@Composable
fun FoodItem() {
    Box(
        modifier = Modifier
            .height(120.dp)
            .width(104.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.tm), // 이미지 리소스 아이디
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop, // 이미지가 박스를 꽉 채우도록 조정
            modifier = Modifier.fillMaxSize() // 박스의 전체 크기에 맞춰 이미지 크기 조정
        )
        // 여기에 음식 아이템 내용 추가
        Column(Modifier.padding(6.dp)) {
            // "흑미밥" 텍스트에 대한 박스
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp)) // 흰색 배경과 둥근 모서리 적용
                    .padding(horizontal = 6.dp, vertical = 2.dp) // 텍스트 주변의 패딩
            ) {
                Text(
                    text = "흑미밥인데",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Pretend,
                    lineHeight = 17.sp,
                    color = Color(0xFF171A1F)
                )
            }
            Spacer(Modifier.height(5.dp))
            // "215kcal" 텍스트에 대한 박스
            Box(
                modifier = Modifier
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp)) // 흰색 배경과 둥근 모서리 적용
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


