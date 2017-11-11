package com.mylisabox.common.room

import com.google.gson.Gson
import com.mylisabox.common.CommonApplication.Companion.WEAR_ROOMS_PATH
import com.mylisabox.common.dashboard.DashboardRepository
import com.mylisabox.common.device.DeviceRepository
import com.mylisabox.common.wearable.Wearable
import com.mylisabox.lisa.network.devices.DeviceApi
import com.mylisabox.network.devices.models.Device
import com.mylisabox.network.room.RoomApi
import com.mylisabox.network.room.models.Room
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class RoomRepository @Inject constructor(private val roomApi: RoomApi,
                                         private val wearable: Wearable,
                                         private val gson: Gson,
                                         private val dashboardRepository: DashboardRepository,
                                         deviceApi: DeviceApi) : DeviceRepository(deviceApi) {
    override fun getDevices(roomId: Long?): Single<Array<Device>> {
        return dashboardRepository.getDashboardForRoom(roomId!!)
    }

    fun retrieveAll(): Single<List<Room>> {
        return roomApi.getRooms()
                .doOnSuccess {
                    wearable.sendData(WEAR_ROOMS_PATH, gson.toJson(it)).subscribeBy(onError = Timber::e)
                }
                .subscribeOn(Schedulers.io())
    }

    fun create(roomName: String): Single<Room> {
        return roomApi.createRoom(Room(null, roomName))
                .subscribeOn(Schedulers.io())
    }

}