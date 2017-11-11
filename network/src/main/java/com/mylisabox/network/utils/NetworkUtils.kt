package com.mylisabox.network.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject

class NetworkUtils @Inject constructor() {
    companion object {
        val TYPE_PROXY = 16 //for wear devices
    }

    fun isWifiActivated(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        var isWifi = false
        if (networkInfo != null) {
            isWifi = networkInfo.isConnected && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == TYPE_PROXY)
        }
        return isWifi
    }
}