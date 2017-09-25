package com.mylisabox.network.dagger.components

import com.mylisabox.network.NetworkApplication
import com.mylisabox.network.dagger.annotations.ApplicationScope
import com.mylisabox.network.dagger.modules.NetworkModule
import dagger.Component

@ApplicationScope
@Component(modules = arrayOf(NetworkModule::class))
interface NetworkComponent {
    fun inject(app: NetworkApplication)
}