package com.example.mealtoyou.ui.theme.shared

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mealtoyou.ui.theme.diet.Diet
import com.example.mealtoyou.ui.theme.main.food.nomal.RecommendationContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DietBox(diet: List<Diet>?, pagerState: PagerState?, b: Boolean) {
    Log.d("식단~~",diet.toString())

    if (diet != null) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (pagerState != null) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                ) { page ->
                    Box(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 1.dp, end = 1.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(8.dp),
                                ambientColor = Color(0xFF171A1F),
                                spotColor = Color(0xFF171A1F)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                    ) {
                        RecommendationContent(b, diet[page])
                    }
                }
            }
            Spacer(modifier = Modifier.height(13.dp))
            if (pagerState != null) {
                if (pagerState.pageCount > 1) {
                    Row(
                        Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
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

        }
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 1.dp, end = 1.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(8.dp),
                        ambientColor = Color(0xFF171A1F),
                        spotColor = Color(0xFF171A1F)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
            ) {
                Column {
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "아직 준비된 데이터가 없어요 ㅠㅠ", fontWeight = FontWeight.SemiBold, color = Color.Black)
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

        }
    }

}

