package com.mylisabox.lisa.wearable

import android.content.Intent
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.mylisabox.common.CommonApplication.Companion.KEY_DATA
import com.mylisabox.common.CommonApplication.Companion.WEAR_DEVICE_ACTION
import com.mylisabox.common.CommonApplication.Companion.WEAR_DEVICE_VALUE_PATH
import com.mylisabox.common.CommonApplication.Companion.WEAR_LOAD_ACTION
import com.mylisabox.common.CommonApplication.Companion.WEAR_LOAD_PATH
import com.mylisabox.common.CommonApplication.Companion.WEAR_SPEECH_ACTION
import com.mylisabox.common.CommonApplication.Companion.WEAR_SPEECH_PATH
import com.mylisabox.lisa.wearable.receivers.WearDeviceDataReceiver
import com.mylisabox.lisa.wearable.receivers.WearLoadDataReceiver
import com.mylisabox.lisa.wearable.receivers.WearSpeechDataReceiver
import timber.log.Timber


class MobileWearableListener : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Timber.e("WEAR message: " + messageEvent.path + " " + messageEvent.sourceNodeId)
        when {
            WEAR_LOAD_PATH == messageEvent.path -> {
                val intent = Intent(applicationContext, WearLoadDataReceiver::class.java)
                intent.action = WEAR_LOAD_ACTION
                intent.putExtra(KEY_DATA, String(messageEvent.data))
                sendBroadcast(intent)
            }
            WEAR_SPEECH_PATH == messageEvent.path -> {
                val intent = Intent(applicationContext, WearSpeechDataReceiver::class.java)
                intent.action = WEAR_SPEECH_ACTION
                intent.putExtra(KEY_DATA, String(messageEvent.data))
                sendBroadcast(intent)
            }
            WEAR_DEVICE_VALUE_PATH == messageEvent.path -> {
                val intent = Intent(applicationContext, WearDeviceDataReceiver::class.java)
                intent.action = WEAR_DEVICE_ACTION
                intent.putExtra(KEY_DATA, String(messageEvent.data))
                sendBroadcast(intent)
            }
        }
    }
}