package com.mylisabox.network.interceptors

import com.mylisabox.network.exceptions.NoEndPointException
import com.mylisabox.network.utils.BaseUrlResolver
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

class HostSelectionInterceptorTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock private lateinit var baseUrlResolver: BaseUrlResolver
    private lateinit var hostSelectionInterceptor: HostSelectionInterceptor

    @Before
    fun setUp() {
        /*
        //this way failing tests are working
        baseUrlResolver = mock {
            //on { getBaseUrl() }.doReturn(Single.just("https://myurlmocked.com"))
            on { getBaseUrl() }.doReturn(Single.error(NoEndPointException()))
        }*/
        hostSelectionInterceptor = HostSelectionInterceptor(baseUrlResolver)
    }

    @Test
    @Ignore("Failed because of mockito/kotlin")
    fun intercept() {
        val chain = Mockito.mock(Interceptor.Chain::class.java)
        val request = Mockito.mock(Request::class.java)
        val response = Mockito.mock(Response::class.java)
        val builder = Mockito.mock(Request.Builder::class.java)
        val url = Mockito.mock(HttpUrl::class.java)
        val urlBuilder = Mockito.mock(HttpUrl.Builder::class.java)
        whenever(chain.request()).thenReturn(request)
        whenever(request.url()).thenReturn(url)
        whenever(url.newBuilder()).thenReturn(urlBuilder)
        whenever(urlBuilder.host(eq("myurlmocked.com"))).thenReturn(urlBuilder)
        whenever(urlBuilder.port(eq(443))).thenReturn(urlBuilder)
        whenever(urlBuilder.scheme("https")).thenReturn(urlBuilder)
        whenever(urlBuilder.build()).thenReturn(url)
        whenever(chain.proceed(request)).thenReturn(response)
        whenever(request.newBuilder()).thenReturn(builder)
        whenever(builder.url(any<HttpUrl>())).thenReturn(builder)
        whenever(builder.build()).thenReturn(request)
        whenever(baseUrlResolver.getBaseUrl()).thenReturn(Single.just("http://myurl.com"))

        hostSelectionInterceptor.intercept(chain)
        assertEquals("https", hostSelectionInterceptor.scheme)
        assertEquals(443, hostSelectionInterceptor.port)
        assertEquals("myurlmocked.com", hostSelectionInterceptor.host)
    }

    @Test(expected = NoEndPointException::class)
    fun interceptNoEndPoint() {
        val chain = Mockito.mock(Interceptor.Chain::class.java)
        val request = Mockito.mock(Request::class.java)
        val url = Mockito.mock(HttpUrl::class.java)
        whenever(chain.request()).thenReturn(request)
        whenever(request.url()).thenReturn(url)
        whenever(baseUrlResolver.getBaseUrl()).thenReturn(Single.error(NoEndPointException()))

        hostSelectionInterceptor.intercept(chain)
    }

    @Test
    fun setBaseUrlToNull() {
        hostSelectionInterceptor.host = "test"
        hostSelectionInterceptor.port = 123
        hostSelectionInterceptor.scheme = "scheme"

        hostSelectionInterceptor.setBaseUrl(null)

        assertEquals("http", hostSelectionInterceptor.scheme)
        assertEquals(3000, hostSelectionInterceptor.port)
        assertNull(hostSelectionInterceptor.host)
    }

    @Test
    fun setBaseUrlToUrl() {
        assertEquals("http", hostSelectionInterceptor.scheme)
        assertEquals(3000, hostSelectionInterceptor.port)
        assertNull(hostSelectionInterceptor.host)

        hostSelectionInterceptor.setBaseUrl("https://mylisabox.com")

        assertEquals("https", hostSelectionInterceptor.scheme)
        assertEquals(443, hostSelectionInterceptor.port)
        assertEquals("mylisabox.com", hostSelectionInterceptor.host)
    }

}