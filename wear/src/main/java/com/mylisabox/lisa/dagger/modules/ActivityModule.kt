package com.mylisabox.lisa.dagger.modules

import android.content.Context
import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: BaseActivity) {

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
}