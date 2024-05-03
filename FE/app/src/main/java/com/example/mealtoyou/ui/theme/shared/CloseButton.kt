package com.example.mealtoyou.ui.theme.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mealtoyou.R

@Composable
fun CloseButton(showTemp: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.xbutton),
        contentDescription = "x button",
        modifier = Modifier
            .size(24.dp)
            .clickable { showTemp.invoke() }
    )
}

@Composable
fun CloseButton(showTemp: MutableState<Boolean>) {
    Image(
        painter = painterResource(id = R.drawable.xbutton),
        contentDescription = "x button",
        modifier = Modifier
            .size(24.dp)
            .clickable { showTemp.value = false }
    )
}