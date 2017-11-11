package com.mylisabox.network.utils

import android.content.Context
import android.net.nsd.NsdManager
import com.mylisabox.network.exceptions.NoEndPointException
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness


class MdnsFinderTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock lateinit var context: Context
    @Mock lateinit var nsdManager: NsdManager
    @Mock lateinit var networkUtils: NetworkUtils
    private lateinit var mdnsFinder: MdnsFinder

    @Before
    fun setUp() {
        mdnsFinder = MdnsFinder(context, nsdManager, networkUtils)
    }

    @Test
    fun searchLISAServiceNoWifiNetwork() {
        whenever(networkUtils.isWifiActivated(any())).thenReturn(false)
        mdnsFinder.searchLISAService().test().assertError(NoEndPointException::class.java).assertTerminated().assertNotComplete()
    }

    @Test
    @Ignore
    fun searchLISAServiceNotFound() {
        whenever(networkUtils.isWifiActivated(any())).thenReturn(true)
        mdnsFinder.searchLISAService().test().assertError(NoEndPointException::class.java).assertTerminated().assertNotComplete()

    }

    @Test
    @Ignore
    fun searchLISAServiceFound() {
        whenever(networkUtils.isWifiActivated(any())).thenReturn(true)
        mdnsFinder.searchLISAService().test().assertError(NoEndPointException::class.java).assertTerminated().assertNotComplete()
    }

}