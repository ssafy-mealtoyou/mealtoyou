package com.example.mealtoyou.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mealtoyou.data.HealthData
import kotlinx.coroutines.flow.MutableStateFlow

class HealthViewModel :ViewModel() {
    val _bodyResult = MutableStateFlow<HealthData?>(null)


}