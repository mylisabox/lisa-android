package com.mylisabox.network.user

import com.mylisabox.network.user.models.User
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UserApi {
    @Multipart
    @POST("user")
    fun update(@PartMap() partMap: Map<String, @JvmSuppressWildcards RequestBody>,
               @Part avatar: MultipartBody.Part?): Single<User>

    @GET("user")
    fun find(): Single<User>

}