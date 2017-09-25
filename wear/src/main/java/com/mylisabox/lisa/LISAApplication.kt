package com.mylisabox.lisa

import com.mylisabox.common.CommonApplication
import com.mylisabox.common.utils.AppPreferences
import com.mylisabox.lisa.common.BaseNavigator
import com.mylisabox.lisa.dagger.components.ApplicationComponent
import com.mylisabox.lisa.dagger.components.DaggerApplicationComponent
import com.mylisabox.lisa.dagger.modules.ApplicationModule
import com.mylisabox.network.preferences.Preferences

class LISAApplication : CommonApplication() {

    lateinit var appComponent: ApplicationComponent
    val baseNavigator = BaseNavigator(this)

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .networkModule(networkModule)
                .build()
        appComponent.inject(this)
    }

    override fun getPreferences(): Preferences {
        return AppPreferences(this)
    }

    override fun goToLogin() {
        baseNavigator.goToLogin()
    }
}