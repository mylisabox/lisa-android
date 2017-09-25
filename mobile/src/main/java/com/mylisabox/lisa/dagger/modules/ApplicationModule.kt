package com.mylisabox.lisa.dagger.modules

import com.mylisabox.common.utils.ImagesProvider
import com.mylisabox.lisa.LISAApplication
import com.mylisabox.lisa.utils.MobileImagesProvider
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.utils.BaseUrlProvider
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: LISAApplication) {
    @Provides
    fun providePreferences(): Preferences {
        return application.getPreferences()
    }

    @Provides
    fun provideImagesProvider(baseUrlProvider: BaseUrlProvider): ImagesProvider {
        return MobileImagesProvider(baseUrlProvider)
    }

    @Provides
    fun provideBaseUrlProvider(): BaseUrlProvider {
        return application
    }
}