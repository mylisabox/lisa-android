package com.mylisabox.network.devices.models

import com.mylisabox.network.utils.IdProvider

data class Device(override val id: Long, var name: String, val pluginName: String, val roomId: Number, var favorite: Boolean,
                  var data: MutableMap<String, Any>, val template: BaseElement, val type: String) : IdProvider