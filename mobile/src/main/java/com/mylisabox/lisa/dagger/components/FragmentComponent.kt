package com.mylisabox.lisa.dagger.components

import com.mylisabox.lisa.dagger.modules.FragmentModule
import com.mylisabox.lisa.device.fragments.DevicesFragment
import com.mylisabox.lisa.device.fragments.FavoritesFragment
import com.mylisabox.lisa.device.fragments.NewDevicesFragment
import com.mylisabox.lisa.device.fragments.RoomFragment
import com.mylisabox.lisa.login.LoginFragment
import com.mylisabox.lisa.settings.SettingsDialogFragment
import com.mylisabox.network.dagger.annotations.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {
    fun inject(fragment: LoginFragment)
    fun inject(fragment: DevicesFragment)
    fun inject(fragment: RoomFragment)
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: NewDevicesFragment)
    fun inject(fragment: SettingsDialogFragment)
}