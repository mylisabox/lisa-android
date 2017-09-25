package com.mylisabox.common.device

import com.mylisabox.common.dashboard.DashboardRepository
import com.mylisabox.lisa.network.devices.DeviceApi
import com.mylisabox.network.devices.FavoriteApi
import com.mylisabox.network.devices.models.Device
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val favoriteApi: FavoriteApi, private val dashboardRepository: DashboardRepository,
                                             deviceApi: DeviceApi) : DeviceRepository(deviceApi) {
    override fun getDevices(roomId: Long?): Single<Array<Device>> {
        return dashboardRepository.getDashboardForRoom(0)
    }

    fun toggleFavorite(device: Device): Completable {
        return if (device.favorite) favoriteApi.deleteFavorite(device.id) else favoriteApi.putFavorite(device.id)
    }
}