package com.mylisabox.common.device

import com.mylisabox.network.devices.models.Device
import io.reactivex.Single

interface DeviceProvider {
    fun getDevices(roomId: Long? = null): Single<Array<Device>>
}