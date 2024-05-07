package com.example.mealtoyou.ui.theme.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.shared.DietBox
import com.example.mealtoyou.ui.theme.shared.MainBar

@Composable
fun GroupPage() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            MainBar(text = "살까기 그룹")
            InfoSection()
            ContentRows()
            DietBox()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InfoSection() {
    val infoModifier = Modifier
        .height(66.dp)
        .fillMaxWidth()
        .padding(top = 8.dp, start = 20.dp, end = 20.dp)
        .defaultShadow()
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFF5F1FE))

    Box(modifier = infoModifier) {
        InfoRow()
    }
}

@Composable
private fun InfoRow() {
    Row(modifier = Modifier.padding(12.dp)) {
        InfoColumn("일일 목표 소모 칼로리", "400 kcal")
        Spacer(modifier = Modifier.weight(1f))
        InfoColumn("일일 목표 걸음 수", "6000걸음")
        Spacer(modifier = Modifier.weight(1f))
        InfoColumn("주별 최소 인증", "3회")
        Spacer(modifier = Modifier.weight(1f))
        InfoColumn("참가 인원", "49명")
    }
}

@Composable
private fun InfoColumn(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 10.sp, textAlign = TextAlign.Center, color = Color(0xFF323743))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 26.sp,
            textAlign = TextAlign.Center
            , color = Color(0xFF323743)
        )
    }
}

@Composable
private fun ContentRows() {
    Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp)) {
        ContentBox(Modifier.weight(1f)) { FirstContent() }
        Spacer(modifier = Modifier.width(16.dp))
        ContentBox(Modifier.weight(1f)) { SecondContent() }
    }
}

@Composable
private fun FirstContent() {
    Column {
        Text("일일 목표", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
        Text("이번 주는 3회 남았어요.", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF323743))
        Spacer(modifier = Modifier.weight(1f))
        Row {
            Column {
                Text("걸음 수", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
                Text("9,281 걸음", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF323743))
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text("소모 칼로리", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
                Text("428 kcal", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF323743))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D31ED)),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .height(36.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = true,
                    ambientColor = Color(0xFF171A1F).copy(alpha = 0.15f),
                    spotColor = Color(0xFF171A1F).copy(alpha = 0.15f)
                ),
        ) {
            Text(text = "일일 목표 인증", fontFamily = Pretend, color = Color.White)
        }
    }
}

@Composable
private fun SecondContent() {
    Column {
        Text("그룹 채팅", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(32.dp)
                .wrapContentWidth()
                .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 8.dp))
                .background(Color(0xFFF3F4F6))
                .padding(top = 3.dp, start = 10.dp, end = 10.dp)
        ) {
            Text(text = "일일 목표 인증", color = Color(0xFF323743), fontWeight = FontWeight.SemiBold, lineHeight = 26.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(32.dp)
                .wrapContentWidth()
                .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 8.dp))
                .background(Color(0xFFF3F4F6))
                .padding(top = 3.dp, start = 10.dp, end = 10.dp)
        ) {
            Text(text = "쿠쿠하세요", color = Color(0xFF323743), fontWeight = FontWeight.SemiBold, lineHeight = 26.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(32.dp)
                .wrapContentWidth()
                .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 8.dp))
                .background(Color(0xFFF3F4F6))
                .padding(top = 3.dp, start = 10.dp, end = 10.dp)
        ) {
            Text(text = "신박하네요?", color = Color(0xFF323743), fontWeight = FontWeight.SemiBold, lineHeight = 26.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
private fun ContentBox(modifier: Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .height(166.dp)
            .defaultShadow()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {
        content()
    }
}

private fun Modifier.defaultShadow() = this.shadow(
    elevation = 2.dp,
    shape = RoundedCornerShape(8.dp),
    ambientColor = Color(0xFF171A1F),
    spotColor = Color(0xFF171A1F)
)


