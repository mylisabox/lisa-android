package com.mylisabox.network.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

class NetworkUtilsTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    private lateinit var networkUtils: NetworkUtils

    @Before
    fun setUp() {
        networkUtils = NetworkUtils()
    }

    @Test
    fun isWifiActivatedOnWifiNetwork() {
        val networkInfo = mock<NetworkInfo> {
            on { isConnected }.doReturn(true)
            on { type }.doReturn(ConnectivityManager.TYPE_WIFI)
        }
        val manager = mock<ConnectivityManager> {
            on { activeNetworkInfo }.doReturn(networkInfo)
        }
        val context = mock<Context> {
            on { getSystemService(Context.CONNECTIVITY_SERVICE) }.doReturn(manager)
        }
        assertTrue(networkUtils.isWifiActivated(context))
    }

    @Test
    fun isWifiActivatedOnNonWifiNetwork() {
        val networkInfo = mock<NetworkInfo> {
            on { isConnected }.doReturn(true)
            on { type }.doReturn(ConnectivityManager.TYPE_MOBILE)
        }
        val manager = mock<ConnectivityManager> {
            on { activeNetworkInfo }.doReturn(networkInfo)
        }
        val context = mock<Context> {
            on { getSystemService(Context.CONNECTIVITY_SERVICE) }.doReturn(manager)
        }
        assertFalse(networkUtils.isWifiActivated(context))
    }

    @Test
    fun isWifiActivatedOnNoNetwork() {
        val manager = mock<ConnectivityManager>()
        val context = mock<Context> {
            on { getSystemService(Context.CONNECTIVITY_SERVICE) }.doReturn(manager)
        }
        assertFalse(networkUtils.isWifiActivated(context))

        val networkInfo = mock<NetworkInfo> {
            on { isConnected }.doReturn(false)
        }
        whenever(manager.activeNetworkInfo).doReturn(networkInfo)

        assertFalse(networkUtils.isWifiActivated(context))
    }

}