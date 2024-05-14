package com.example.mealtoyou.data.repository

import com.example.mealtoyou.MainApplication
import com.example.mealtoyou.data.model.request.LoginReqDto
import com.example.mealtoyou.retrofit.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepository {
    fun login(loginReqDto: LoginReqDto): Flow<Boolean> = flow {
        val response = RetrofitClient.authInstance.login(loginReqDto)
        if (response.code() == 200) {
            response.body()?.let {
                MainApplication.prefs.setValue("accessToken", response.body()!!.accessToken)
                MainApplication.prefs.setValue("refreshToken", response.body()!!.refreshToken)
                MainApplication.prefs.setValue("userId",response.body()!!.userId)
                emit(true)
            }
        } else {
            emit(false)
        }
    }
//    fun reissue(): Flow<Boolean> = flow {
//        val response = RetrofitClient.authInstance.reissue(loginReqDto)
//
//        if (response.code() == 200) {
//            MainApplication.prefs.setValue("accessToken", response.body()!!.accessToken)
//            emit(true)
//        } else {
//            emit(false)
//        }
//    }
}