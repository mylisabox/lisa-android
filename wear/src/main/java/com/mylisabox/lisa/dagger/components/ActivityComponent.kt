package com.mylisabox.lisa.dagger.components

import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.lisa.dagger.modules.ActivityModule
import com.mylisabox.lisa.dagger.modules.ViewModelModule
import com.mylisabox.lisa.home.HomeActivity
import com.mylisabox.lisa.login.LoginActivity
import com.mylisabox.lisa.splashscreen.SplashScreenActivity
import com.mylisabox.network.dagger.annotations.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class, ViewModelModule::class))
interface ActivityComponent {
    fun inject(activity: BaseActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: HomeActivity)
    fun inject(activity: SplashScreenActivity)
}