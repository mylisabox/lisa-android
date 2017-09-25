package com.mylisabox.network.dagger.modules

import android.content.Context
import android.net.nsd.NsdManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mylisabox.lisa.network.devices.DeviceApi
import com.mylisabox.network.NetworkApiProvider
import com.mylisabox.network.NetworkApplication
import com.mylisabox.network.chatbot.ChatBotApi
import com.mylisabox.network.dagger.annotations.Qualifiers.ForApplication
import com.mylisabox.network.dashboard.DashboardApi
import com.mylisabox.network.devices.FavoriteApi
import com.mylisabox.network.devices.models.BaseElement
import com.mylisabox.network.preferences.PreferencesProvider
import com.mylisabox.network.room.RoomApi
import com.mylisabox.network.user.LoginApi
import com.mylisabox.network.utils.BaseElementParser
import com.mylisabox.network.utils.RxErrorForwarder.LoginNavigation
import dagger.Module
import dagger.Provides


@Module
class NetworkModule(private val application: NetworkApplication) {
    @Provides
    fun provideLoginNavigation(): LoginNavigation {
        return application
    }

    @Provides
    @ForApplication
    fun provideAppContext(): Context {
        return application
    }

    @Provides
    fun provideApplication(): NetworkApplication {
        return application
    }

    @Provides
    fun providePreferencesProvider(): PreferencesProvider {
        return application
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(BaseElement::class.java, BaseElementParser())
                .create()
    }

    @Provides
    fun provideLoginApi(networkApiProvider: NetworkApiProvider): LoginApi {
        return networkApiProvider.create(LoginApi::class.java)
    }

    @Provides
    fun provideChatBotApi(networkApiProvider: NetworkApiProvider): ChatBotApi {
        return networkApiProvider.create(ChatBotApi::class.java)
    }

    @Provides
    fun provideFavoriteApi(networkApiProvider: NetworkApiProvider): FavoriteApi {
        return networkApiProvider.create(FavoriteApi::class.java)
    }

    @Provides
    fun provideDashboardApi(networkApiProvider: NetworkApiProvider): DashboardApi {
        return networkApiProvider.create(DashboardApi::class.java)
    }

    @Provides
    fun provideRoomApi(networkApiProvider: NetworkApiProvider): RoomApi {
        return networkApiProvider.create(RoomApi::class.java)
    }

    @Provides
    fun provideDeviceApi(networkApiProvider: NetworkApiProvider): DeviceApi {
        return networkApiProvider.create(DeviceApi::class.java)
    }

    @Provides
    fun provideNsdManager(@ForApplication context: Context): NsdManager {
        return context.getSystemService(Context.NSD_SERVICE) as NsdManager
    }

}