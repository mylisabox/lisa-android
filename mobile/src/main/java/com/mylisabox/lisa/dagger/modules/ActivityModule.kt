package com.mylisabox.lisa.dagger.modules

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.mylisabox.common.dagger.ViewModelFactory
import com.mylisabox.common.device.BaseTemplateBuilderVisitor
import com.mylisabox.common.utils.DimensionUtils
import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.lisa.common.TemplateMobileViewBuilder
import com.mylisabox.lisa.home.MainMenuViewModel
import com.mylisabox.lisa.login.LoginViewModel
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import com.mylisabox.network.utils.BaseUrlProvider
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: BaseActivity) {

    @Provides
    @ForActivity
    fun provideLoginViewModel(factory: ViewModelFactory): LoginViewModel {
        return ViewModelProviders.of(activity, factory).get(LoginViewModel::class.java)
    }

    @Provides
    @ForActivity
    fun provideMainMenuViewModel(factory: ViewModelFactory): MainMenuViewModel {
        return ViewModelProviders.of(activity, factory).get(MainMenuViewModel::class.java)
    }

    @Provides
    @ForActivity
    fun provideActivity(): BaseActivity {
        return activity
    }

    @Provides
    @ForActivity
    fun provideActivityContext(): Context {
        return activity
    }

    @Provides
    fun provideBaseTemplateBuilderVisitor(context: Context, baseUrlProvider: BaseUrlProvider, dimensionUtils: DimensionUtils): BaseTemplateBuilderVisitor {
        return TemplateMobileViewBuilder(context, baseUrlProvider, dimensionUtils)
    }
}