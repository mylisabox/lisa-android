package com.mylisabox.lisa.dagger.modules

import com.mylisabox.lisa.LISAApplication
import com.mylisabox.network.preferences.Preferences
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: LISAApplication) {
    @Provides
    fun providePreferences(): Preferences {
        return application.getPreferences()
    }

}