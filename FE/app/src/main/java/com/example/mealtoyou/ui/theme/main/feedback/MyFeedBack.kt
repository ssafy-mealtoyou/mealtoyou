package com.example.mealtoyou.ui.theme.main.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.viewmodel.AIFeedbackViewModel

@Composable
fun FeedbackSection(
    feedbackTag: String,
    mainMessage: String,
    detailMessage: String
) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8F9FA))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight() // 내용에 맞게 높이 조정
        ) {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFDCFC4))
                    .padding(start = 4.dp, end = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = feedbackTag,
                    color = Color(0xFFDE3B40),
                    fontSize = 8.sp,
                    fontFamily = Pretend,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 8.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mainMessage,
                color = Color(0xFF323743),
                fontSize = 12.sp,
                lineHeight = 20.sp,
                fontFamily = Pretend,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}



@Preview
@Composable
fun TodayFeedBackBox(aiFeedbackViewModel: AIFeedbackViewModel = viewModel()) {

    val feedbackResponse = aiFeedbackViewModel.feedbackResponse

    // 스크롤을 맨 아래로 이동
    LaunchedEffect(true) {
        aiFeedbackViewModel.updateAIFeedBack()
    }

    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color(0xFF171A1F),
                spotColor = Color(0xFF171A1F)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = "피드백",
                color = Color(0xFF9095A1),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                fontFamily = Pretend
            )
            Spacer(Modifier.height(8.dp))
            FeedbackSection(
                feedbackTag = feedbackResponse.title.ifEmpty { "AI 피드백" },
                mainMessage = feedbackResponse.content.ifEmpty { "준비중 입니다." },
                detailMessage = ""
            )
//            Spacer(Modifier.height(12.dp))
//            FeedbackSection(
//                feedbackTag = "탄수화물 과다",
//                mainMessage = "정제된 탄수화물이 든 음식을 줄여요.",
//                detailMessage = "Dolor minim eu ipsum commodo nulla eu sint velit dolore velit nulla sunt Lorem excepteur nostrud ullamco irure ad."
//            )
        }
    }
}

@Composable
fun MyFeedBack() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
            .padding(end = 20.dp)
            .padding(bottom = 20.dp)
    ) {
        TodayFeedBackBox()
    }
}