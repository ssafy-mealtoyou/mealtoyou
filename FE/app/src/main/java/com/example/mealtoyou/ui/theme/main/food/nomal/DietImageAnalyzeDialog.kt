package com.example.mealtoyou.ui.theme.main.food.nomal

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mealtoyou.data.repository.DietImageRepository
import com.example.mealtoyou.data.repository.DietRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
fun DietImageAnalyzeDialog(
    onImageSelected: (Bitmap) -> Unit // 이미지 선택 콜백 추가
) {
    // 상태 관리를 위한 remember 사용
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Composable 함수 내에서 LocalContext.current 캡처
    val context = LocalContext.current

    // 사진 촬영 또는 갤러리 선택 후 결과 처리에서 이미지 선택 콜백 호출
    val takePicturePreviewLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let { onImageSelected(it) } // 선택한 이미지 콜백으로 전달
        }
    )

    val pickImageLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val imageStream: InputStream? = context.contentResolver.openInputStream(uri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                onImageSelected(selectedImage) // 선택한 이미지 콜백으로 전달
            }
        }
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { takePicturePreviewLauncher.launch(null) }) {
            Text("카메라로 사진 찍기")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { pickImageLauncher.launch("image/*") }) {
            Text("갤러리에서 사진 선택")
        }
//        imageBitmap?.let { bitmap ->
//            Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.size(200.dp))
//        }
    }
}

@Composable
fun ShowDietImageAnalyzeDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onImageSelected: (Bitmap) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            // Dialog의 배경색과 모양 설정
            Surface(
                shape = MaterialTheme.shapes.medium, // 모달의 모양
                color = MaterialTheme.colorScheme.surface // 모달의 배경색
            ) {
                DietImageAnalyzeDialog(onImageSelected = onImageSelected)
            }
        }
    }
}

@Composable
fun TestDietImageAnalyzeDialogComponent() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var logText by remember {
        mutableStateOf("")
    }

    val dietRepository: DietImageRepository = remember {
        DietImageRepository
    }

    // Composable 함수 내부
    val coroutineScope = rememberCoroutineScope()

    suspend fun uploadImage(bitmap: Bitmap) {
        val response = dietRepository.analyzeImage(bitmap, "foodImage");

        if (response.isSuccessful) {
            // 성공적으로 업로드됨
            logText = "성공적으로 업로드됨"
        } else {
            // 업로드 실패
            logText = "성공적으로 업로드됨"
        }
        logText += "\n${response.headers().toList()}"
        logText += "\n${response.body().toString()}"
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // 이미지 선택 버튼
        Button(onClick = { showDialog = true }) {
            Text("이미지 선택 다이얼로그 열기")
        }

        // 선택된 이미지 표시
        selectedImageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }

        // 다이얼로그 표시
        ShowDietImageAnalyzeDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false }, // 다이얼로그 닫기
            onImageSelected = { bitmap ->
                coroutineScope.launch {
                    uploadImage(bitmap)
                }
                selectedImageBitmap = bitmap
                showDialog = false // 이미지 선택 후 다이얼로그 닫기
            }
        )

        Text(text = logText)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTestDietImageAnalyzeDialogComponent() {
    TestDietImageAnalyzeDialogComponent()
}


