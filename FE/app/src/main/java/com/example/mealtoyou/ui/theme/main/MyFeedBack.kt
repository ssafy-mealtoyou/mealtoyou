package com.example.mealtoyou.ui.theme.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TodayFeedBackBox() {
    Box(
        modifier = Modifier
            .height(216.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color(0xFF171A1F),
                spotColor = Color(0xFF171A1F)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
    }
}

@Composable
fun MyFeedBack() {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
            .padding(end = 20.dp)
            .padding(bottom = 20.dp)
    ) {
        TodayFeedBackBox()
    }
}