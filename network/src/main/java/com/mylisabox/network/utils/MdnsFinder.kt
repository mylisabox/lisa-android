package com.mylisabox.network.utils

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import android.os.Handler
import com.mylisabox.network.dagger.annotations.ApplicationScope
import com.mylisabox.network.dagger.annotations.Qualifiers.ForApplication
import com.mylisabox.network.exceptions.NoEndPointException
import io.reactivex.Single
import timber.log.Timber
import java.net.Inet4Address
import javax.inject.Inject

@ApplicationScope
class MdnsFinder @Inject constructor(@ForApplication private val appContext: Context,
                                     private val nsdManager: NsdManager,
                                     private val networkUtils: NetworkUtils) {
    companion object {
        private val SEARCH_TIMEOUT = 5000L
        private val SERVICE_TYPE = "_http._tcp."
        private val SERVICE_NAME = "LISA"
    }

    private var discoveryListener: NsdManager.DiscoveryListener? = null
    private val handler = Handler()
    private var runnable: Runnable? = null

    var onLISAFoundListener: OnLISAFoundListener? = null

    interface OnLISAFoundListener {
        fun onLISAFound(serviceInfo: NsdServiceInfo)

        fun onError(error: Throwable)
    }

    private fun start() {
        if (discoveryListener != null) {
            this.stop()
        }
        discoveryListener = object : NsdManager.DiscoveryListener {

            //  Called as soon as service discovery begins.
            override fun onDiscoveryStarted(regType: String) {
                Timber.d("Service discovery started")
            }

            override fun onServiceFound(service: NsdServiceInfo) {
                // A service was found!  Do something with it.
                Timber.d("Service discovery success" + service)
                if (service.serviceName.contains(SERVICE_NAME)) {
                    nsdManager.resolveService(service, object : NsdManager.ResolveListener {

                        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                            if (serviceInfo.host is Inet4Address) {
                                onLISAFoundListener?.onLISAFound(serviceInfo)
                            }
                        }

                        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                            Timber.d("Service resolve failed!")
                            onLISAFoundListener?.onError(Exception("onResolveFailed"))
                        }
                    })
                }
            }

            override fun onServiceLost(service: NsdServiceInfo) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Timber.e("service lost" + service)
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Timber.d("Discovery stopped: " + serviceType)
                stop()
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                Timber.e("Discovery failed: Error code:" + errorCode)
                stop()
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Timber.e("Discovery failed: Error code:" + errorCode)
                stop()
            }
        }
        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }

    private fun stop() {
        if (discoveryListener != null)
            nsdManager.stopServiceDiscovery(discoveryListener)
        discoveryListener = null
    }

    fun searchLISAService(): Single<String> {
        return Single.create({ subscriber ->
            if (Build.FINGERPRINT != null && Build.FINGERPRINT.toLowerCase().contains("generic")) {
                subscriber.onSuccess("http://10.0.2.2:3000")
            } else if (networkUtils.isWifiActivated(appContext)) {
                onLISAFoundListener = object : OnLISAFoundListener {
                    override fun onLISAFound(serviceInfo: NsdServiceInfo) {
                        handler.removeCallbacks(runnable)
                        if (!subscriber.isDisposed) {
                            subscriber.onSuccess("http:/${serviceInfo.host}:${serviceInfo.port}")
                        }
                        stop()
                    }

                    override fun onError(error: Throwable) {
                        handler.removeCallbacks(runnable)
                        if (!subscriber.isDisposed)
                            subscriber.onError(error)
                        stop()
                    }
                }
                start()
                runnable = Runnable {
                    stop()//Not found under 4s
                    if (!subscriber.isDisposed)
                        subscriber.onError(NoEndPointException())
                }

                handler.postDelayed(runnable, SEARCH_TIMEOUT)
            } else {
                if (!subscriber.isDisposed)
                    subscriber.onError(NoEndPointException())
            }
        })
    }
}