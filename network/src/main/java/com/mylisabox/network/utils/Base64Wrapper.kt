package com.mylisabox.network.utils

import android.util.Base64
import javax.inject.Inject

class Base64Wrapper @Inject constructor() {
    fun decode(input: String, flags: Int): String {
        return String(Base64.decode(input, flags))
    }
}