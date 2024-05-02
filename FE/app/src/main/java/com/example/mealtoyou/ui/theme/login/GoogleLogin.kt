package com.example.mealtoyou.ui.theme.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.R
import com.example.mealtoyou.ui.theme.Pretend


@Composable
fun GoogleButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()  // 화면 전체를 채우도록 설정
            .padding(bottom = 50.dp)  // 하단에서 37.5dp 떨어진 위치에 버튼을 배치
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .height(52.dp)
                .width(348.dp)
                .align(Alignment.BottomCenter)  // 버튼을 Box의 하단 중앙에 정렬
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(50.dp),
                    clip = true,
                    ambientColor = Color(0xFF171A1F).copy(alpha = 0.15f),
                    spotColor = Color(0xFF171A1F).copy(alpha = 0.15f)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))  // 이미지와 텍스트 사이 간격
                Text(
                    text = "Google 계정으로 시작",
                    color = Color(0xFF171A1F),
                    fontSize = 16.sp,
                    fontFamily = Pretend,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally),
                )
            }
        }
    }
}