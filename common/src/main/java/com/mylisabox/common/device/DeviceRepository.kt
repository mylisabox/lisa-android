package com.mylisabox.common.device

import com.mylisabox.lisa.network.devices.DeviceApi
import com.mylisabox.lisa.network.devices.models.DeviceValue
import com.mylisabox.network.devices.models.Device
import com.mylisabox.network.devices.models.WidgetEvent
import io.reactivex.Single

abstract class DeviceRepository(protected val deviceApi: DeviceApi) : DeviceProvider {
    fun saveWidgetValue(widgetEvent: WidgetEvent<*>): Single<Device> {
        if (widgetEvent.device.id > 1000) {
            return deviceApi.postGroupValue(widgetEvent.device.roomId, widgetEvent.device.type, DeviceValue(widgetEvent.key, widgetEvent.value!!))
                    .andThen(Single.just(widgetEvent.device))
        }
        return deviceApi.postDeviceValue(widgetEvent.device.pluginName, widgetEvent.device.id, DeviceValue(widgetEvent.key, widgetEvent.value!!))
    }
}