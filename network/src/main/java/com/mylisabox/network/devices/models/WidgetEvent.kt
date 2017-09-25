package com.mylisabox.network.devices.models

data class WidgetEvent<out Any>(val device: Device,
                                val key: String,
                                val value: Any)