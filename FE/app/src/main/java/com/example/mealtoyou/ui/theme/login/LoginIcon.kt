package com.example.mealtoyou.ui.theme.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.R
import com.example.mealtoyou.ui.theme.Pretend

@Composable
fun MainIcon() {
    BoxWithConstraints {
        Column {
            Image(
                painter = painterResource(id = R.drawable.main_icon),
                contentDescription = "Main Icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(58.dp)
                    .padding(start = 35.dp, bottom = 0.dp),  // 이미지 왼쪽과 하단에서 10dp 떨어지게 설정
                alignment = Alignment.BottomStart  // 이미지를 하단 왼쪽으로 정렬
            )

            Text(
                text = "Let's start your\nhealth journey\ntoday with us!",
                modifier = Modifier
                    .padding(start = 37.dp, top = 16.dp),
                fontSize = 32.sp, // 폰트 사이즈 설정
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 48.sp,
                fontFamily = Pretend
            )
        }
    }

}
