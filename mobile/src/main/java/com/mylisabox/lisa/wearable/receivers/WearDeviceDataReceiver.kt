package com.mylisabox.lisa.wearable.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.mylisabox.common.CommonApplication.Companion.KEY_DATA
import com.mylisabox.common.room.RoomRepository
import com.mylisabox.lisa.LISAApplication
import com.mylisabox.lisa.network.devices.DeviceApi
import javax.inject.Inject

class WearDeviceDataReceiver : BroadcastReceiver() {
    var isInjected = false
    @Inject
    lateinit var deviceRepository: RoomRepository

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var deviceApi: DeviceApi

    override fun onReceive(context: Context, intent: Intent) {
        if (!isInjected) {
            isInjected = true
            val app = context.applicationContext as LISAApplication
            app.appComponent.inject(this)
        }
        val data = intent.getStringExtra(KEY_DATA)
        //val event = gson.fromJson(data, DeviceValueInfos::class.java)
        //deviceApi?.sendDeviceValue(event)
    }
}