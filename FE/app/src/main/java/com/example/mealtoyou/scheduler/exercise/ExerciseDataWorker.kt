import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.mealtoyou.data.ExerciseData
import com.example.mealtoyou.retrofit.RetrofitClient

class ExerciseDataWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val healthConnectClient: HealthConnectClient =
            HealthConnectClient.getOrCreate(context = applicationContext)
        val zoneId = ZoneId.of("Asia/Seoul") // 한국 시간대로 설정
        val startOfToday = LocalDate.now(zoneId).minusDays(1).atStartOfDay(zoneId).toInstant()

        val permissions = setOf(
            // 걸음 수 데이터 읽기 권한
            HealthPermission.createReadPermission(StepsRecord::class),
            HealthPermission.createReadPermission(TotalCaloriesBurnedRecord::class)
        )

        val granted = healthConnectClient.permissionController.getGrantedPermissions(permissions)
        if (permissions == granted) {
            val stepRequest = ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(
                    startOfToday, Instant.now()
                )
            )
            val caloriesRequest = ReadRecordsRequest(
                recordType = TotalCaloriesBurnedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(
                    startOfToday, Instant.now()
                )
            )
            try {
                val stepResponse = healthConnectClient.readRecords(stepRequest)
                val caloriesResponse = healthConnectClient.readRecords(caloriesRequest)
                val steps = stepResponse.records.firstOrNull()?.count ?: 0
                val stepStartDate = stepResponse.records.firstOrNull()?.startTime?.atZone(zoneId)?.toLocalDate() ?: LocalDate.now(zoneId)
                val caloriesBurned = caloriesResponse.records.firstOrNull()?.energy?.inKilocalories ?: 0.0
                val caloriesStartDate = caloriesResponse.records.firstOrNull()?.startTime?.atZone(zoneId)?.toLocalDate() ?: LocalDate.now(zoneId)

                Log.d("data", "Exercise Data - Steps: $steps, Step Start Date: $stepStartDate, Calories Burned: $caloriesBurned, Calories Start Date: $caloriesStartDate")

                val exerciseData = ExerciseData(steps, stepStartDate, caloriesBurned, caloriesStartDate)
                sendExerciseData(exerciseData)
                return Result.success() // Indicate that the work finished successfully
            } catch (e: Exception) {
                Log.e("HealthData", "Error reading health data: ${e.message}")
                return Result.retry() // Indicate that the work should be retried
            }
        } else {
            Log.e("HealthData", "Required permissions not granted")
            return Result.failure() // Indicate that the work failed
        }
    }

    private suspend fun sendExerciseData(exerciseData: ExerciseData) {
        try {
            val response = RetrofitClient.healthInstance.postExerciseData(
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVTTBCSFpOU3FMZENLN2hOV20xYnJnPT0iLCJpYXQiOjE3MTUxNDA4NzMsImV4cCI6MTcxNTIyNzI3M30.ZGIfU6HbKmcvvv75EzX0Y5uN2SaiAI8NTtpJ09yDsDk",
                exerciseData
            )
            if (response.isSuccessful) {
                Log.d("API", "Data sent successfully")
            } else {
                Log.e("API", "Failed to send data, response code: ${response.code()} + ${response.message()}")
            }
        } catch (e: Throwable) {
            Log.e("API", "Error sending data: ${e.message}")
        }
    }
}
