package com.mylisabox.lisa.dagger.modules

import android.arch.lifecycle.ViewModelProvider
import com.mylisabox.common.dagger.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}