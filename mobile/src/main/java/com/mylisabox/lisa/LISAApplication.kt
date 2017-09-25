package com.mylisabox.lisa

import com.bumptech.glide.request.target.ViewTarget
import com.mylisabox.common.CommonApplication
import com.mylisabox.common.utils.AppPreferences
import com.mylisabox.lisa.common.BaseNavigator
import com.mylisabox.lisa.dagger.components.ApplicationComponent
import com.mylisabox.lisa.dagger.components.DaggerApplicationComponent
import com.mylisabox.lisa.dagger.modules.ApplicationModule
import com.mylisabox.network.preferences.Preferences

class LISAApplication : CommonApplication() {

    lateinit var appComponent: ApplicationComponent
    private val baseNavigator = BaseNavigator(this)

    override fun onCreate() {
        super.onCreate()
        ViewTarget.setTagId(R.id.glide_tag)

        appComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .networkModule(networkModule)
                .build()
        appComponent.inject(this)

    }

    override fun goToLogin() {
        baseNavigator.goToLogin()
    }

    override fun getPreferences(): Preferences {
        return AppPreferences(this)
    }
}