package com.mylisabox.network.interceptors

import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.preferences.PreferencesProvider
import okhttp3.Interceptor
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

class TokenInterceptor @Inject constructor(private val preferencesProvider: PreferencesProvider) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val newRequest: Request

        val token: String? = preferencesProvider.getPreferences().get(Preferences.KEY_TOKEN)
        val newRequestBuilder = request.newBuilder().addHeader("Accept", "application/json")
        if (token != null) {
            newRequestBuilder.addHeader("Authorization", "JWT $token")
        }
        newRequest = newRequestBuilder.build()
        return chain.proceed(newRequest)
    }
}