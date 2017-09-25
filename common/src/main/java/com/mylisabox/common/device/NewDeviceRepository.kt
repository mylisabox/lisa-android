package com.mylisabox.common.device

import com.mylisabox.lisa.network.devices.DeviceApi
import com.mylisabox.network.devices.models.Device
import io.reactivex.Single
import javax.inject.Inject

class NewDeviceRepository @Inject constructor(deviceApi: DeviceApi) : DeviceRepository(deviceApi) {
    override fun getDevices(roomId: Long?): Single<Array<Device>> {
        return deviceApi.getNewDevices()
    }

}