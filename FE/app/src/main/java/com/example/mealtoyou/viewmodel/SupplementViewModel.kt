import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoyou.data.SupplementResponseData
import com.example.mealtoyou.data.UserIdData
import com.example.mealtoyou.data.repository.SupplementRepository
import com.example.mealtoyou.retrofit.RetrofitClient
import com.example.mealtoyou.ui.theme.diet.Diet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SupplementViewModel : ViewModel() {
    private val _supplementResult = MutableStateFlow<List<SupplementResponseData>?>(null)
    val supplementResult = _supplementResult.asStateFlow()
    private val dietApiService = RetrofitClient.dietInstance
    private val _diets = MutableStateFlow<List<Diet>>(emptyList())
    val diets: StateFlow<List<Diet>> get() = _diets

    fun loadDiets(userId: Int) {
        viewModelScope.launch {
            try {
                val result = dietApiService.recommendDiet(UserIdData(user_id = userId))
                _diets.value = result
            } catch (e: Exception) {
            }
        }
    }

    fun supplementScreen() {
        viewModelScope.launch {

            if (_supplementResult.value.isNullOrEmpty()) { // 데이터가 비어 있거나 초기 상태(null)인 경우에만 로드
                Log.d("model","model")
                _supplementResult.value = SupplementRepository.getSupplements()
                Log.d("aaaa","${_supplementResult.value}")
            }
        }
    }

}
