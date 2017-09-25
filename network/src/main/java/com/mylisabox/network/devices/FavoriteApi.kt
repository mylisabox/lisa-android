package com.mylisabox.network.devices

import com.mylisabox.network.devices.models.Device
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FavoriteApi {
    @GET("favorite")
    fun getFavorites(): Single<Array<Device>>

    @PUT("favorite/{id}")
    fun putFavorite(@Path("id") id: Long): Completable

    @DELETE("favorite/{id}")
    fun deleteFavorite(@Path("id") id: Long): Completable
}