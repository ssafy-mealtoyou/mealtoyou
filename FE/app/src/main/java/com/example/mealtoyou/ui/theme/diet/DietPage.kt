package com.example.mealtoyou.ui.theme.diet

import CalendarScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mealtoyou.ui.theme.shared.DietBox
import com.example.mealtoyou.ui.theme.shared.MainBar

@Composable
fun DietPage() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            MainBar(text = "식단", infoImg = true)
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxHeight()
            ) {
                CalendarScreen()
                Spacer(modifier = Modifier.height(16.dp))
                DietTotalCal()
                Spacer(modifier = Modifier.height(16.dp))
                DietInfographic()
                Column(Modifier.padding(start = 20.dp, end = 20.dp)) {
                    DietBox()
                }

                Spacer(modifier = Modifier.height(16.dp))

            }
        }

    }
}