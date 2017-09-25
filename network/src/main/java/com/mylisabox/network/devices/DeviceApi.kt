package com.mylisabox.lisa.network.devices

import com.mylisabox.lisa.network.devices.models.DeviceValue
import com.mylisabox.network.devices.models.Device
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface DeviceApi {
    @PATCH("device/{id}")
    fun patchDevice(@Path("id") id: Number, @Body device: Map<String, Any>): Single<Device>

    @DELETE("device/{id}")
    fun deleteDevice(@Path("id") id: Number): Single<Device>

    @POST("plugins/{name}/{deviceId}")
    fun postDeviceValue(@Path("name") pluginName: String,
                        @Path("deviceId") deviceId: Number,
                        @Body device: DeviceValue): Single<Device>

    @POST("devices/group/{roomId}/{type}")
    fun postGroupValue(@Path("roomId") roomId: Number,
                       @Path("type") type: String,
                       @Body device: DeviceValue): Completable

    @GET("device?roomId=null")
    fun getNewDevices(): Single<Array<Device>>
}