import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoyou.data.FoodRequestItem
import com.example.mealtoyou.data.SwipeFoodItemModel
import com.example.mealtoyou.data.repository.DietRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class DietCreationViewModel : ViewModel() {

    private val dietRepository by lazy {
        DietRepository
    }

    fun createDietFromSwipeFoodItems(foodRequestItemList: List<SwipeFoodItemModel>) {
        createDiet(foodRequestItemList.map {item -> FoodRequestItem(item.fid, item.quantity)}.toList())
    }

    private fun createDiet(foodRequestItemList: List<FoodRequestItem>) {
        viewModelScope.launch {
            try {
                val result: Response<Void> = dietRepository.createDiet(foodRequestItemList)
                if (result.isSuccessful) {
                    // 요청 성공
                    Log.d("DietCreation", "요청 성공: ${result.code()}")
                } else {
                    // 요청 실패 (예: 서버 오류)
                    Log.d("DietCreation", "요청 실패: ${result.errorBody()?.string() ?: "알 수 없는 오류"}")
                }
            } catch (e: Exception) {
                // 네트워크 오류 등의 예외 처리
                Log.d("DietCreation", "예외 발생: ${e.message}")
            }
        }
    }

}
