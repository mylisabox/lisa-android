package com.mylisabox.lisa.dagger.modules

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import com.mylisabox.common.dagger.ViewModelFactory
import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.lisa.home.MainMenuViewModel
import com.mylisabox.lisa.login.LoginViewModel
import com.mylisabox.network.dagger.annotations.Qualifiers.ForFragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: Fragment) {
    @Provides
    @ForFragment
    fun provideLoginViewModel(factory: ViewModelFactory): LoginViewModel {
        return ViewModelProviders.of(fragment, factory).get(LoginViewModel::class.java)
    }

    @Provides
    @ForFragment
    fun provideMainMenuViewModel(factory: ViewModelFactory): MainMenuViewModel {
        return ViewModelProviders.of(fragment, factory).get(MainMenuViewModel::class.java)
    }

    @Provides
    @ForFragment
    fun provideContext(): Context {
        return fragment.activity as Context
    }

    @Provides
    @ForFragment
    fun provideActivity(): BaseActivity {
        return fragment.activity as BaseActivity
    }
}