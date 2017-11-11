package com.mylisabox.lisa.wearable

import android.content.Intent
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.mylisabox.common.CommonApplication
import com.mylisabox.lisa.wearable.receivers.MobileLoggedOutReceiver

class WearWearableListener : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        when {
            CommonApplication.WEAR_LOGGED_OUT_PATH == messageEvent.path -> {
                val intent = Intent(applicationContext, MobileLoggedOutReceiver::class.java)
                intent.action = CommonApplication.WEAR_LOGGED_OUT_PATH
                sendBroadcast(intent)
            }
        }
    }
}