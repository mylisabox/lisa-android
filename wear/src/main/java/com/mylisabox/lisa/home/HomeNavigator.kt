package com.mylisabox.lisa.home

import android.content.Context
import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.lisa.common.BaseNavigator
import com.mylisabox.lisa.room.RoomActivity
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import javax.inject.Inject

class HomeNavigator @Inject constructor(@ForActivity private val context: Context) : BaseNavigator(context) {
    fun goToSpeech() {
        if (context is BaseActivity) {
            context.goToSpeech()
        }
    }

    fun goToFavorites() {
        TODO("to be done")
    }

    fun goToNewDevices() {
        TODO("to be done")
    }

    fun goToRoom(roomId: Long) {
        context.startActivity(RoomActivity.newInstance(context, roomId))
    }
}