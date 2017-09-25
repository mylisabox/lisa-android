package com.mylisabox.common

import android.os.StrictMode
import com.mylisabox.network.BuildConfig
import com.mylisabox.network.NetworkApplication
import timber.log.Timber

abstract class CommonApplication : NetworkApplication() {
    companion object {
        val KEY_EMAIL = "KEY_EMAIL"
        val KEY_THEME = "KEY_THEME"
        val KEY_ROOM = "room"
        val KEY_DEVICE = "device"

        val WEAR_ROOMS_PATH = "/lisa/rooms"
        val WEAR_THEME_PATH = "/lisa/theme"
        val WEAR_ROOM_PATH = "/lisa/room"
        val WEAR_LOAD_PATH = "/lisa/load"
        val WEAR_SPEECH_PATH = "/lisa/speech"
        val WEAR_DEVICE_VALUE_PATH = "/lisa/device/value"
        val KEY_DATA = "data"

        val WEAR_LOAD_ACTION = "load"
        val WEAR_SPEECH_ACTION = "speech"
        val WEAR_DEVICE_ACTION = "device"
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    //.penaltyDeath()
                    .build())
            Timber.plant(Timber.DebugTree())
        }
    }

}