package com.example.mealtoyou.handler

import android.content.Context
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.mealtoyou.data.ExerciseData
import com.example.mealtoyou.data.HealthData
import com.example.mealtoyou.retrofit.RetrofitClient
import com.example.mealtoyou.viewmodel.HealthViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class HealthEventHandler(private val lifecycleOwner: LifecycleOwner, private val healthConnectClient: HealthConnectClient) {
    private val zoneId = ZoneId.of("Asia/Seoul") // 한국 시간대로 설정
    private val startOfToday = LocalDate.now(zoneId).minusDays(1).atStartOfDay(zoneId).toInstant() // 오늘 자정의 Instant, 한국 시간대 기준


    fun readHealthData(viewModel : HealthViewModel, onSuccess: () -> Unit) {
        lifecycleOwner.lifecycleScope.launch {
            val permissions = setOf(
                HealthPermission.createReadPermission(BasalMetabolicRateRecord::class),
                HealthPermission.createReadPermission(BodyFatRecord::class)
            )
            val granted =
                healthConnectClient.permissionController.getGrantedPermissions(permissions)
            if (permissions == granted) {
                val bmrRequest = ReadRecordsRequest(
                    recordType = BasalMetabolicRateRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(
                        startOfToday, Instant.now()
                    )
                )
                val bodyFatRequest = ReadRecordsRequest(
                    recordType = BodyFatRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(
                        startOfToday, Instant.now()
                    )
                )

                val weightRequest = ReadRecordsRequest(
                    recordType = WeightRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(
                        startOfToday, Instant.now()
                    )
                )
                try {
                    val bmrResponse = healthConnectClient.readRecords(bmrRequest)
                    val bodyFatResponse = healthConnectClient.readRecords(bodyFatRequest)
                    val weightResponse = healthConnectClient.readRecords(weightRequest)
                    val bmr =
                        bmrResponse.records.firstOrNull()?.basalMetabolicRate?.inKilocaloriesPerDay
                            ?: 0.0
                    val bodyFat = bodyFatResponse.records.firstOrNull()?.percentage?.value ?: 0.0
                    val measuredDate =
                        bodyFatResponse.records.firstOrNull()?.time?.atZone(zoneId)  // Instant를 ZoneId에 맞는 ZonedDateTime으로 변환
                            ?.toLocalDate() ?: LocalDate.now(zoneId)// null 경우 처리
                    val weight = weightResponse.records.firstOrNull()?.weight?.inKilograms?:0.0
                    val skeletalMuscle = (weight - (bodyFat/100) * weight) * 0.4
                    Log.d("health","${bmr} + ${bodyFat} + ${measuredDate} + ${weight} + ${skeletalMuscle}")

                    val healthData = HealthData(bmr, measuredDate, bodyFat,skeletalMuscle,weight)
//                    if (bmr !== 0.0 && bodyFat !== 0.0 && weight !== 0.0 && skeletalMuscle !== 0.0) {
                    sendHealthData(healthData = healthData) {
                        onSuccess.invoke()
                    }
                    viewModel._bodyResult.value= HealthData(bmr, measuredDate, bodyFat, skeletalMuscle, weight)
//                    }
                } catch (e: Exception) {
                    Log.e("HealthData", "Error reading health data: ${e.message}")
                }
            } else {
                Log.e("HealthData", "Required permissions not granted")
            }

        }
    }

    fun sendHealthData(healthData: HealthData, onSuccess: () -> Unit) {
        RetrofitClient.healthInstance.postHealthData(healthData).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                    Log.d("API", "Data sent successfully")
                } else {
                    Log.e("API", "Failed to send data, response code: ${response.code()} + ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "Error sending data: ${t.message}")
            }
        })
    }


    suspend fun readExerciseData(): Pair<Int, Int>? {
        // 권한 확인
        val permissions = setOf(
            // 걸음 수 데이터 읽기 권한
            HealthPermission.createReadPermission(StepsRecord::class),
            HealthPermission.createReadPermission(TotalCaloriesBurnedRecord::class)
        )

        val granted = healthConnectClient.permissionController.getGrantedPermissions(permissions)
        return if (permissions == granted) {
            val stepRequest = ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startOfToday, Instant.now())
            )
            val caloriesRequest = ReadRecordsRequest(
                recordType = TotalCaloriesBurnedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startOfToday, Instant.now())
            )
            try {
                val stepResponse = healthConnectClient.readRecords(stepRequest)
                val caloriesResponse = healthConnectClient.readRecords(caloriesRequest)
                val steps = stepResponse.records.firstOrNull()?.count ?: 0
                val stepStartDate = stepResponse.records.firstOrNull()?.startTime?.atZone(zoneId)
                    ?.toLocalDate() ?: LocalDate.now(zoneId) // null 경우 처리
                val caloriesBurned = caloriesResponse.records.firstOrNull()?.energy?.inKilocalories ?: 0.0
                val caloriesStartDate = caloriesResponse.records.firstOrNull()?.startTime?.atZone(zoneId)
                    ?.toLocalDate() ?: LocalDate.now(zoneId) // null 경우 처리
                Log.d("start", "$startOfToday")
                val exerciseData = ExerciseData(steps, stepStartDate, caloriesBurned, caloriesStartDate)
                Log.d(
                    "data", "Exercise Data - Steps: ${exerciseData.steps} (${exerciseData.steps::class.simpleName}), " +
                            "Step Start Date: ${exerciseData.stepStartDate} (${exerciseData.stepStartDate::class.simpleName}), " +
                            "Calories Burned: ${exerciseData.caloriesBurned} (${exerciseData.caloriesBurned::class.simpleName}), " +
                            "Calories Start Date: ${exerciseData.caloriesStartDate} (${exerciseData.caloriesStartDate::class.simpleName})"
                )
                Log.d("health", "${exerciseData.steps} + ${exerciseData.stepStartDate} + ${exerciseData.caloriesBurned} + ${exerciseData.caloriesStartDate}")
                sendExerciseData(exerciseData = exerciseData)
                Pair(steps.toInt(), caloriesBurned.toInt())
            } catch (e: Exception) {
                Log.e("HealthData", "Error reading health data: ${e.message}")
                null
            }
        } else {
            Log.e("HealthData", "Required permissions not granted")
            null
        }
    }

    private suspend fun sendExerciseData(exerciseData: ExerciseData) {
        try {
            val response = RetrofitClient.healthInstance.postExerciseData(exerciseData)
            if (response.isSuccessful) {
                Log.d("API", "Data sent successfully")
            } else {
                Log.e("API", "Failed to send data, response code: ${response.code()} + ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("API", "Error sending data: ${e.message}")
        }
    }


}