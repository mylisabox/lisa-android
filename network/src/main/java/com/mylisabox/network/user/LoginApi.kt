package com.mylisabox.network.user

import com.mylisabox.lisa.network.users.models.Credential
import com.mylisabox.network.user.models.InitializedResponse
import com.mylisabox.network.user.models.LoginResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/local")
    fun login(@Body credential: Credential): Single<LoginResponse>

    @POST("auth/local/register")
    fun register(@Body credential: Credential): Single<LoginResponse>

    @GET("initialized")
    fun initialized(): Single<InitializedResponse>

    @GET("auth/logout")
    fun logout(): Completable
}