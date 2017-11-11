package com.mylisabox.common.user

import android.content.Context
import android.net.Uri
import com.mylisabox.common.CommonApplication
import com.mylisabox.lisa.network.users.models.Credential
import com.mylisabox.network.dagger.annotations.Qualifiers.ForApplication
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.user.LoginApi
import com.mylisabox.network.user.UserApi
import com.mylisabox.network.user.models.User
import com.mylisabox.network.utils.TokenUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class UserRepository @Inject constructor(@ForApplication private val context: Context,
                                         private val loginApi: LoginApi,
                                         private val userApi: UserApi,
                                         private val preferences: Preferences,
                                         private val tokenUtils: TokenUtils) {
    fun getProfile(): Single<User> {
        return userApi.find()
    }

    fun updateProfile(user: User, avatar: File?): Single<User> {
        return Single.defer({
            var formData: MultipartBody.Part? = null
            if (avatar != null) {
                val type = context.contentResolver.getType(Uri.fromFile(avatar))
                // create RequestBody instance from file
                val requestFile = RequestBody.create(
                        if (type == null) null else MediaType.parse(type),
                        avatar
                )

                formData = MultipartBody.Part.createFormData("avatar", avatar.name, requestFile)
            }
            val map = HashMap<String, RequestBody>()
            if (user.firstName != null) {
                map.put("firstname", createPartFromString(user.firstName!!))
            }

            if (user.lastName != null) {
                map.put("lastname", createPartFromString(user.lastName!!))
            }

            map.put("email", createPartFromString(user.email))

            if (user.mobile != null) {
                map.put("mobile", createPartFromString(user.mobile!!))
            }

            if (user.password != null) {
                map.put("password", createPartFromString(user.password!!))
            }
            userApi.update(map, formData)
        }).subscribeOn(Schedulers.io())
    }

    private fun createPartFromString(data: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), data)
    }

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