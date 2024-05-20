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
            elevation = 5.dp,  // 그림자 높이 증가
            shape = RoundedCornerShape(8.dp),
            ambientColor = Color.Black,  // 상단 그림자를 조금 더 진하게
            spotColor = Color(0xFF171A1F)  // 하단 그림자를 조금 더 연하게
        )
        .clip(RoundedCornerShape(8.dp))
        .background(Color.White)
}