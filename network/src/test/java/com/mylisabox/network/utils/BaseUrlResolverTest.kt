package com.mylisabox.network.utils

import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.preferences.PreferencesProvider
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

internal class BaseUrlResolverTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock private lateinit var mdns: MdnsFinder
    @Mock private lateinit var preferencesProvider: PreferencesProvider
    private lateinit var baseUrlResolver: BaseUrlResolver

    @Before
    fun setUp() {
        baseUrlResolver = BaseUrlResolver(mdns, preferencesProvider)
    }

    @Test
    fun testGetBaseUrlSuccess() {
        whenever(mdns.searchLISAService()).thenReturn(Single.just("http://baseUrl"))
        val baseUrl = baseUrlResolver.getBaseUrl().test().awaitCount(1).assertNoErrors().assertComplete().values()[0]

        assertEquals("http://baseUrl", baseUrl)
    }

    @Test
    fun testGetBaseUrlErrorWithRemoteUrl() {
        whenever(mdns.searchLISAService()).thenReturn(Single.error(Exception()))
        val preferences = mock(Preferences::class.java)
        whenever(preferencesProvider.getPreferences()).thenReturn(preferences)
        whenever(preferences.get(anyString(), nullable(String::class.java))).thenReturn("http://prefUrl")
        val baseUrl = baseUrlResolver.getBaseUrl().test().awaitCount(1).assertNoErrors().assertComplete().values()[0]

        assertEquals("http://prefUrl", baseUrl)
    }

    @Test
    fun testGetBaseUrlErrorWithoutRemoteUrl() {
        val exception = Exception()
        whenever(mdns.searchLISAService()).thenReturn(Single.error(exception))
        val preferences = mock(Preferences::class.java)
        whenever(preferencesProvider.getPreferences()).thenReturn(preferences)
        whenever(preferences.get(anyString(), nullable(String::class.java))).thenReturn(null)
        baseUrlResolver.getBaseUrl().test().assertError(exception).assertNotComplete().assertTerminated()
    }

}
