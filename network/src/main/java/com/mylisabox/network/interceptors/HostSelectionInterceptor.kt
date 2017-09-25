package com.mylisabox.network.interceptors

import com.mylisabox.network.dagger.annotations.ApplicationScope
import com.mylisabox.network.utils.BaseUrlResolver
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

//@see https://gist.github.com/swankjesse/8571a8207a5815cca1fb
@ApplicationScope
class HostSelectionInterceptor @Inject constructor(baseUrlResolver: BaseUrlResolver) : Interceptor {
    @Volatile
    var host: String? = null
    @Volatile
    var port: Int = 3000
    @Volatile
    var scheme: String = "http"

    private var getBaseUrl = baseUrlResolver.getBaseUrl().toObservable().share()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        val requestHost = request.url().host()
        val host = this.host
        if (host == null) {
            setBaseUrl(getBaseUrl.blockingFirst())
            request = buildNewRequest(request, this.host!!, this.port, this.scheme)
        } else if (requestHost != host) {
            request = buildNewRequest(request, host, port, scheme)
        }
        return chain.proceed(request)
    }

    fun setBaseUrl(baseUrl: String?) {
        if (baseUrl == null) {
            host = null
            port = 3000
            scheme = "http"
        } else {
            val url = HttpUrl.parse(baseUrl)
            this.host = url?.host()
            this.port = url?.port() ?: 3000
            this.scheme = url?.scheme() ?: "http"
        }
    }

    private fun buildNewRequest(oldRequest: Request, host: String, port: Int, scheme: String): Request {
        val newUrl = oldRequest.url().newBuilder()
                .host(host)
                .port(port)
                .scheme(scheme)
                .build()
        return oldRequest.newBuilder()
                .url(newUrl)
                .build()
    }
}