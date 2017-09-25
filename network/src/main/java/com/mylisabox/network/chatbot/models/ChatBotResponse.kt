package com.mylisabox.network.chatbot.models

data class ChatBotResponse(val botId: String,
                           val action: String,
                           val userSentence: String,
                           val lang: String,
                           val responses: Array<String>,
                           val response: String)