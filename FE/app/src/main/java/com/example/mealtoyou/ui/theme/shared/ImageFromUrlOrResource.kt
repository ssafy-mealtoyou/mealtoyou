package com.example.mealtoyou.ui.theme.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ImageFromUrlOrResource(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    defaultImageId: Int // 기본 이미지 리소스 ID를 인자로 받음
) {
    if (imageUrl.isNullOrEmpty()) {
        // URL이 null이거나 비어있는 경우 로컬 리소스 사용
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(defaultImageId) // 기본 이미지 리소스 ID
                .build(),
            contentDescription = "Sample Food Image",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        // URL이 유효한 경우 해당 URL에서 이미지 로드
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Remote Image",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}
