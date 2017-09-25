package com.mylisabox.common.user

import com.mylisabox.common.CommonApplication
import com.mylisabox.lisa.network.users.models.Credential
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.user.LoginApi
import com.mylisabox.network.user.models.User
import com.mylisabox.network.utils.TokenUtils
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(private val loginApi: LoginApi,
                                         private val preferences: Preferences,
                                         private val tokenUtils: TokenUtils) {

    fun retrieve(credential: Credential): Single<User> {
        return loginApi.login(credential)
                .doOnSuccess {
                    preferences.edit()
                    preferences.set(Preferences.KEY_TOKEN, it.token)
                    preferences.set(CommonApplication.KEY_EMAIL, credential.email)
                    preferences.apply()
                }
                .map { it.user }
    }

    fun create(credential: Credential): Single<User> {
        return loginApi.register(credential)
                .doOnSuccess {
                    preferences.edit()
                    preferences.set(Preferences.KEY_TOKEN, it.token)
                    preferences.set(CommonApplication.KEY_EMAIL, credential.email)
                    preferences.apply()
                }
                .map { it.user }
    }

    fun retrieveFromToken(): User? {
        val token: String? = preferences.get(Preferences.KEY_TOKEN)
        return tokenUtils.getUserFromToken(token)
    }

    fun logoutUser(): Completable {
        return loginApi.logout()
    }
}