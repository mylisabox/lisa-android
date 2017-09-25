package com.mylisabox.network.room

import com.mylisabox.network.room.models.Room
import io.reactivex.Single
import retrofit2.http.*

interface RoomApi {
    @GET("room?sort=name")
    fun getRooms(): Single<List<Room>>

    @POST("room")
    fun createRoom(@Body room: Room): Single<Room>

    @PUT("room/{id}")
    fun updateRoom(@Path("id") id: String, @Body room: Room): Single<Room>

    @PATCH("room/{id}")
    fun patchRoom(@Path("id") id: String, @Body room: Room): Single<Room>

    @DELETE("room/{id}")
    fun deleteRoom(@Path("id") id: String): Single<Room>

}