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
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni: NetworkInfo? = cm.activeNetworkInfo
        var br = false
        if (ni != null) {
            br = ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == TYPE_PROXY)
        }
        return br
    }
}