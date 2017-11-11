package com.mylisabox.lisa.wearable.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.mylisabox.common.CommonApplication
import com.mylisabox.common.CommonApplication.Companion.KEY_DATA
import com.mylisabox.common.CommonApplication.Companion.KEY_ROOM
import com.mylisabox.common.room.RoomRepository
import com.mylisabox.common.wearable.Wearable
import com.mylisabox.lisa.LISAApplication
import javax.inject.Inject


class WearLoadDataReceiver : BroadcastReceiver() {
    private var isInjected = false
    @Inject
    lateinit var wearable: Wearable
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var roomsRepository: RoomRepository

    override fun onReceive(context: Context, intent: Intent) {
        val needToLoad = intent.getStringExtra(KEY_DATA)
        if (!isInjected) {
            isInjected = true
            val app = context.applicationContext as LISAApplication
            app.appComponent.inject(this)
        }
        if (needToLoad == KEY_ROOM) {
            roomsRepository.retrieveAll()
                    .flatMapCompletable { wearable.sendData(CommonApplication.WEAR_ROOMS_PATH, gson.toJson(it)) }
                    .subscribe()
        } else {
            roomsRepository.getDevices(needToLoad.toLong())
                    .flatMapCompletable { wearable.sendMessage(CommonApplication.WEAR_ROOMS_PATH, gson.toJson(it)) }
                    .subscribe()
        }

    }
}