package com.example.mealtoyou.ui.theme.main.stage

import SupplementViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MyStage(supplementViewModel: SupplementViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
            .padding(end = 20.dp)
            .padding(bottom = 20.dp)
    ) {
        DrugInfo(Modifier.weight(8f), Color.White, false, supplementViewModel)
        Spacer(Modifier.weight(1f))
        Challenge(Modifier.weight(8f), Color.White, false)
    }
}