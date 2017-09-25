package com.mylisabox.network.devices.models

import com.mylisabox.network.devices.models.builder.Template
import com.mylisabox.network.devices.models.builder.TemplateBuilder
import com.mylisabox.network.devices.models.builder.TemplatePopulator
import io.reactivex.subjects.PublishSubject
import java.io.Serializable
import java.lang.ref.WeakReference

abstract class BaseElement : TemplateBuilder, TemplatePopulator, Serializable {
    var flex: Float = 1f
    var path: String? = null
    var widgetWidth: Int = 1
    var widgetHeight: Int = 1
    lateinit var type: String
    var name: String? = null
    var value: String? = null
    var values: Map<String, String>? = HashMap()
    var infos: Map<String, Any>? = HashMap()

    @Transient lateinit var device: WeakReference<Device>

    open fun associateDevice(device: Device) {
        this.device = WeakReference(device)
    }

    @Transient
    var template: Template? = null

    @Transient
    val onChange: PublishSubject<WidgetEvent<Any>> = PublishSubject.create()
}
