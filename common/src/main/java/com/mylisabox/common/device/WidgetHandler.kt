package com.mylisabox.common.device

import com.mylisabox.network.devices.models.Device
import com.mylisabox.network.devices.models.WidgetEvent
import io.reactivex.Observable

interface WidgetHandler {
    fun listenForWidgetEvents(observable: Observable<WidgetEvent<Any>>)
    fun toggleFavorite(device: Device)
}