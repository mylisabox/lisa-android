package com.mylisabox.common.room

import com.mylisabox.common.dashboard.DashboardRepository
import com.mylisabox.common.device.DeviceRepository
import com.mylisabox.lisa.network.devices.DeviceApi
import com.mylisabox.network.devices.models.Device
import com.mylisabox.network.room.RoomApi
import com.mylisabox.network.room.models.Room
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RoomRepository @Inject constructor(private val roomApi: RoomApi,
                                         private val dashboardRepository: DashboardRepository,
                                         deviceApi: DeviceApi) : DeviceRepository(deviceApi) {
    override fun getDevices(roomId: Long?): Single<Array<Device>> {
        return dashboardRepository.getDashboardForRoom(roomId!!)
    }

    fun retrieveAll(): Single<List<Room>> {
        return roomApi.getRooms()
                .subscribeOn(Schedulers.io())
    }

    fun create(roomName: String): Single<Room> {
        return roomApi.createRoom(Room(null, roomName))
                .subscribeOn(Schedulers.io())
    }

}