package com.mylisabox.lisa.dagger.components

import com.mylisabox.lisa.LISAApplication
import com.mylisabox.lisa.dagger.modules.ActivityModule
import com.mylisabox.lisa.dagger.modules.ApplicationModule
import com.mylisabox.lisa.wearable.receivers.WearDeviceDataReceiver
import com.mylisabox.lisa.wearable.receivers.WearLoadDataReceiver
import com.mylisabox.lisa.wearable.receivers.WearSpeechDataReceiver
import com.mylisabox.network.dagger.annotations.ApplicationScope
import com.mylisabox.network.dagger.modules.NetworkModule
import dagger.Component

@ApplicationScope
@Component(modules = arrayOf(ApplicationModule::class, NetworkModule::class))
interface ApplicationComponent {
    fun plusActivityComponent(activityModule: ActivityModule): ActivityComponent
    fun inject(application: LISAApplication)
    fun inject(wearSpeechDataReceiver: WearSpeechDataReceiver)
    fun inject(wearDeviceDataReceiver: WearDeviceDataReceiver)
    fun inject(wearLoadDataReceiver: WearLoadDataReceiver)

}