package com.example.mealtoyou.ui.theme.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@SuppressLint("ModifierFactoryExtensionFunction")
@Composable
fun shadowModifier(): Modifier {
    return Modifier
        .shadow(
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            ambientColor = Color(0xFF171A1F),
            spotColor = Color(0xFF171A1F)
        )
        .clip(RoundedCornerShape(8.dp))
        .background(Color.White)
        .padding(10.dp)
}