package com.mylisabox.network.dashboard.models

import com.mylisabox.network.devices.models.Device

data class Dashboard(val id: String, val widgets: Array<Device>, val roomId: String, val userId: String)