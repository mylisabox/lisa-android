package com.mylisabox.network.user.models

data class Token(
        val iat: Long = 0,
        val exp: Long = 0,
        val iss: String? = null,
        val aud: String? = null,
        val user: User? = null
)