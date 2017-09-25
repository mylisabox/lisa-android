package com.mylisabox.network

import com.google.gson.Gson
import com.mylisabox.network.dagger.annotations.ApplicationScope
import com.mylisabox.network.interceptors.HostSelectionInterceptor
import com.mylisabox.network.interceptors.TokenInterceptor
import com.mylisabox.network.utils.TagSocketFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@ApplicationScope
class NetworkApiProvider @Inject constructor(urlInterceptor: HostSelectionInterceptor,
                                             tokenInterceptor: TokenInterceptor,
                                             gson: Gson,
                                             tagSocketFactory: TagSocketFactory) {
    private val retrofit: Retrofit

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val client = OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(urlInterceptor)
                .addInterceptor(interceptor)
                .socketFactory(tagSocketFactory)
        retrofit = Retrofit.Builder()
                .baseUrl("http://mylisabox:3000/api/v1/")
                .client(client.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    fun <T> create(toCreate: Class<T>): T {
        return retrofit.create(toCreate)
    }
}