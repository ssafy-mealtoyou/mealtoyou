package com.example.mealtoyou.ui.theme.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
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
fun MainBar(text:String) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 17.dp)
            .padding(bottom = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_main), // 이 부분에 실제 리소스 ID를 사용하세요.
            contentDescription = "Main Icon",
            modifier = Modifier
                .align(Alignment.TopStart) // 왼쪽 상단 정렬
                .padding(start = 20.dp)
        )

        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp), // 가운데 상단 정렬
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF171A1F),
            fontFamily = Pretend
        )

        Image(
            painter = painterResource(id = R.drawable.avatar), // 이 부분에 실제 리소스 ID를 사용하세요.
            contentDescription = "Avatar",
            modifier = Modifier
                .align(Alignment.TopEnd) // 오른쪽 상단 정렬
                .padding(end = 18.dp)
        )
    }

}