package com.mylisabox.network.chatbot

import com.mylisabox.network.chatbot.models.ChatBotResponse
import com.mylisabox.network.chatbot.models.InteractRequest
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatBotApi {
    @POST("chatbot/interact")
    fun interact(@Body speechResquest: InteractRequest): Single<ChatBotResponse>
}