package com.mylisabox.lisa.dagger.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mylisabox.common.dagger.ViewModelFactory
import com.mylisabox.common.dagger.annotations.ViewModelKey
import com.mylisabox.lisa.device.viewmodels.FavoriteViewModel
import com.mylisabox.lisa.device.viewmodels.NewDeviceViewModel
import com.mylisabox.lisa.device.viewmodels.RoomViewModel
import com.mylisabox.lisa.home.MainMenuViewModel
import com.mylisabox.lisa.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainMenuViewModel::class)
    internal abstract fun bindSearchViewModel(viewModel: MainMenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RoomViewModel::class)
    internal abstract fun bindRoomViewModel(viewModel: RoomViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    internal abstract fun bindFavoriteViewModel(viewModel: FavoriteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewDeviceViewModel::class)
    internal abstract fun bindNewDeviceViewModel(viewModel: NewDeviceViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}