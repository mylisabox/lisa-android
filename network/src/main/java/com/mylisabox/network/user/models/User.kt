package com.mylisabox.network.user.models

import com.google.gson.annotations.SerializedName

data class User(val id: Number, val email: String, val avatar: String?, @SerializedName("firstname") val firstName: String?,
                val lang: String?, @SerializedName("lastname") val lastName: String?, val mobile: String?, val password: String?)