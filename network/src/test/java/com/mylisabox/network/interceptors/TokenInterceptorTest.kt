package com.mylisabox.network.interceptors

import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.preferences.Preferences.Companion.KEY_TOKEN
import com.mylisabox.network.preferences.PreferencesProvider
import com.nhaarman.mockito_kotlin.whenever
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

class TokenInterceptorTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock private lateinit var preferencesProvider: PreferencesProvider
    private lateinit var tokenInterceptor: TokenInterceptor

    @Before
    fun setUp() {
        tokenInterceptor = TokenInterceptor(preferencesProvider)
    }

    @Test
    fun interceptWithToken() {
        val chain = mock(Interceptor.Chain::class.java)
        val request = mock(Request::class.java)
        val response = mock(Response::class.java)
        val builder = mock(Request.Builder::class.java)
        whenever(chain.request()).thenReturn(request)
        whenever(chain.proceed(request)).thenReturn(response)
        whenever(request.newBuilder()).thenReturn(builder)
        whenever(builder.build()).thenReturn(request)
        whenever(builder.addHeader(anyString(), anyString())).thenReturn(builder)
        val preferences = mock(Preferences::class.java)
        whenever(preferencesProvider.getPreferences()).thenReturn(preferences)
        whenever(preferences.get<String>(KEY_TOKEN, null)).thenReturn("token")
        tokenInterceptor.intercept(chain)

        verify(builder).addHeader(eq("Accept"), eq("application/json"))
        verify(builder, times(0)).addHeader(eq("Content-Type"), eq("application/json"))
        verify(builder).addHeader(eq("Authorization"), eq("JWT token"))
        verify(builder, times(2)).addHeader(anyString(), anyString())
    }

    @Test
    fun interceptWithoutToken() {
        val chain = mock(Interceptor.Chain::class.java)
        val request = mock(Request::class.java)
        val response = mock(Response::class.java)
        val builder = mock(Request.Builder::class.java)
        whenever(chain.request()).thenReturn(request)
        whenever(chain.proceed(request)).thenReturn(response)
        whenever(request.newBuilder()).thenReturn(builder)
        whenever(builder.build()).thenReturn(request)
        whenever(builder.addHeader(anyString(), anyString())).thenReturn(builder)
        val preferences = mock(Preferences::class.java)
        whenever(preferencesProvider.getPreferences()).thenReturn(preferences)
        whenever(preferences.get<String>(KEY_TOKEN, null)).thenReturn(null)
        tokenInterceptor.intercept(chain)

        verify(builder).addHeader(eq("Accept"), eq("application/json"))
        verify(builder, times(0)).addHeader(eq("Content-Type"), eq("application/json"))
        verify(builder, times(1)).addHeader(anyString(), anyString())
    }

}