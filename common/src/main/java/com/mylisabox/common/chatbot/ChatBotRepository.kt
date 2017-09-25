package com.mylisabox.common.chatbot

import com.mylisabox.network.chatbot.ChatBotApi
import com.mylisabox.network.chatbot.models.ChatBotResponse
import com.mylisabox.network.chatbot.models.InteractResquest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChatBotRepository @Inject constructor(private val chatBotApi: ChatBotApi) {
    fun interact(sentence: String): Single<ChatBotResponse> {
        return chatBotApi.interact(InteractResquest(sentence)).subscribeOn(Schedulers.io())
    }
}