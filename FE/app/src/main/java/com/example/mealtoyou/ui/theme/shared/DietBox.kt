package com.example.mealtoyou.ui.theme.shared

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mealtoyou.ui.theme.main.food.nomal.RecommendationContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DietBox() {
    val pagerState = rememberPagerState(
        pageCount = { 10 }  // 총 페이지 수
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.height(400.dp).fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .height(380.dp)
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 20.dp, end = 20.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(8.dp),
                        ambientColor = Color(0xFF171A1F),
                        spotColor = Color(0xFF171A1F)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
            ) {
                RecommendationContent(false)
            }
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .size(9.dp)
                        .background(color)
                )
            }
        }
    }
}

