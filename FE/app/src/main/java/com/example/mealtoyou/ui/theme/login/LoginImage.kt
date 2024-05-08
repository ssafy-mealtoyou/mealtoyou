package com.example.mealtoyou.ui.theme.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.mealtoyou.R

@Composable
fun MainImage() {
    BoxWithConstraints {
        val screenHeight = maxHeight
        val halfScreenHeight = screenHeight / 1.85f

        Image(
            painter = painterResource(id = R.drawable.main_image),
            contentDescription = "Main Image",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = halfScreenHeight),
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter
        )
    }
}