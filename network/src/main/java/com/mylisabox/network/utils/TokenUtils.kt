package com.mylisabox.network.utils

import android.icu.util.Calendar
import android.os.Build
import com.google.gson.Gson
import com.mylisabox.network.user.models.Token
import com.mylisabox.network.user.models.User
import javax.inject.Inject

class TokenUtils @Inject constructor(private val gson: Gson, private val base64Wrapper: Base64Wrapper) {
    fun isTokenExpired(token: String?): Boolean {
        var expired = true
        val parts = token?.split("\\.".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        if (parts?.size == 3) {
            val response = gson.fromJson<Token>(base64Wrapper.decode(parts[1], 0), Token::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                expired = response.exp * 1000 < Calendar.getInstance().timeInMillis
            } else {
                expired = response.exp * 1000 < System.currentTimeMillis()
            }
        }
        return expired
    }

    fun getUserFromToken(token: String?): User? {
        var user: User? = null
        val parts = token?.split("\\.".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        if (parts?.size == 3) {
            val response = gson.fromJson<Token>(base64Wrapper.decode(parts[1], 0), Token::class.java)
            user = response.user
        }
        return user
    }
}