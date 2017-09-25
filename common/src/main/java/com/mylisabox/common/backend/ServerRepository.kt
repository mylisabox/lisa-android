package com.mylisabox.common.backend

import com.mylisabox.network.user.LoginApi
import io.reactivex.Single
import javax.inject.Inject

class ServerRepository @Inject constructor(private val loginApi: LoginApi) {
    fun isInitialized(): Single<Boolean> {
        return loginApi.initialized().map { it.initialized }
    }
}