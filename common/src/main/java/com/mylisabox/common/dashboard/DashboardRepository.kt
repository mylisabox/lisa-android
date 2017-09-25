package com.mylisabox.common.dashboard

import com.mylisabox.network.dashboard.DashboardApi
import com.mylisabox.network.devices.models.Device
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val dashboardApi: DashboardApi) {
    fun saveWidgetOrder(roomId: Long, devices: List<Device>): Completable {
        return dashboardApi.saveDashboardForRoom(roomId, devices.map { device -> device.id })
    }

    fun getDashboardForRoom(roomId: Long): Single<Array<Device>> {
        return dashboardApi.getDashboardForRoom(roomId).map { it.widgets }
    }
}