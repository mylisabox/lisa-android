package com.mylisabox.lisa.dagger.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mylisabox.common.dagger.ViewModelFactory
import com.mylisabox.common.dagger.annotations.ViewModelKey
import com.mylisabox.lisa.home.HomeViewModel
import com.mylisabox.lisa.room.RoomViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RoomViewModel::class)
    internal abstract fun bindRoomViewModel(viewModel: RoomViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}