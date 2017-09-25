package com.mylisabox.network

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.akaita.java.rxjava2debug.RxJava2Debug
import com.mylisabox.network.dagger.components.DaggerNetworkComponent
import com.mylisabox.network.dagger.modules.NetworkModule
import com.mylisabox.network.interceptors.HostSelectionInterceptor
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.preferences.Preferences.Companion.KEY_TOKEN
import com.mylisabox.network.preferences.PreferencesProvider
import com.mylisabox.network.utils.BaseUrlProvider
import com.mylisabox.network.utils.NetworkUtils
import com.mylisabox.network.utils.RxErrorForwarder.LoginNavigation
import javax.inject.Inject

abstract class NetworkApplication : Application(), PreferencesProvider, LoginNavigation, BaseUrlProvider {
    @Inject lateinit var urlInterceptor: HostSelectionInterceptor
    @Inject lateinit var networkUtils: NetworkUtils

    lateinit var networkModule: NetworkModule
    private val networkChange = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
            if (noConnectivity || networkUtils.isWifiActivated(context)) {
                urlInterceptor.setBaseUrl(null)// let the interceptor do the search of network
            } else {
                urlInterceptor.setBaseUrl(getPreferences().get(Preferences.KEY_EXTERNAL_URL))
            }
        }
    }

    override fun getToken(): String? {
        return getPreferences().get(KEY_TOKEN)
    }

    override fun getBaseUrl(): String {
        return "${urlInterceptor.scheme}://${urlInterceptor.host}:${urlInterceptor.port}"
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            RxJava2Debug.enableRxJava2AssemblyTracking()
        }
        networkModule = NetworkModule(this)
        DaggerNetworkComponent.builder().networkModule(networkModule).build().inject(this)
        registerReceiver(networkChange, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}