package com.mylisabox.lisa.wearable.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mylisabox.common.CommonApplication.Companion.KEY_DATA
import com.mylisabox.common.chatbot.ChatBotRepository
import com.mylisabox.lisa.LISAApplication
import javax.inject.Inject

class WearSpeechDataReceiver : BroadcastReceiver() {
    var isInjected = false
    @Inject
    lateinit var chatBotRepository: ChatBotRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (!isInjected) {
            isInjected = true
            val app = context.applicationContext as LISAApplication
            app.appComponent.inject(this)
        }
        val sentence = intent.getStringExtra(KEY_DATA)
        chatBotRepository.interact(sentence).subscribe()
    }
}