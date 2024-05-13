import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.mealtoyou.api.SupplementApiService
import com.example.mealtoyou.data.SupplementResponseData
import com.example.mealtoyou.data.repository.SupplementRepository
import retrofit2.Response

class SupplementViewModel : ViewModel() {
    private val _supplementResult = MutableStateFlow<List<SupplementResponseData>?>(null)
    val supplementResult = _supplementResult.asStateFlow()

    fun supplementScreen() {
        viewModelScope.launch {
            if (_supplementResult.value.isNullOrEmpty()) { // 데이터가 비어 있거나 초기 상태(null)인 경우에만 로드
                Log.d("model","model")
                _supplementResult.value = SupplementRepository.getSupplements()
                Log.d("aaaa","${_supplementResult.value}")
            }
        }
    }
//    private val apiService: SupplementApiService
//    private val _supplements = MutableStateFlow<List<SupplementResponseData>?>(null)
//    val supplements = _supplements.asStateFlow()
//
//    init {
//        loadSupplements()
//    }
//
//    private fun loadSupplements() {
//        viewModelScope.launch {
//            try {
//                val response = apiService.getSupplements("Bearer your_token_here")
//                if (response.isSuccessful) {
//                    _supplements.value = response.body() // No need to cast here
//                } else {
//                    // Handle error case
//                }
//            } catch (e: Exception) {
//                // Handle exception
//            }
//        }
//    }
}