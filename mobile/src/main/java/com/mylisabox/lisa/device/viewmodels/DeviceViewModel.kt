package com.mylisabox.lisa.device.viewmodels

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.annotation.VisibleForTesting
import com.mylisabox.common.BaseViewModel
import com.mylisabox.common.CommonApplication
import com.mylisabox.common.dashboard.DashboardRepository
import com.mylisabox.common.device.DeviceRepository
import com.mylisabox.common.device.FavoriteRepository
import com.mylisabox.common.device.TemplateViewPopulator
import com.mylisabox.common.device.WidgetHandler
import com.mylisabox.lisa.common.TemplateMobileViewBuilder
import com.mylisabox.network.devices.models.Device
import com.mylisabox.network.devices.models.WidgetEvent
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.utils.RxErrorForwarder
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

abstract class DeviceViewModel(private val preferences: Preferences,
                               private val deviceRepository: DeviceRepository,
                               private val favoriteRepository: FavoriteRepository,
                               private val dashboardRepository: DashboardRepository,
                               private val rxErrorForwarder: RxErrorForwarder,
                               templateViewPopulator: TemplateViewPopulator) : BaseViewModel(), WidgetHandler {
    var templateMobileViewBuilder: TemplateMobileViewBuilder? = null

    var isDragEnabled: Boolean = true
    var roomName: String? = null
    var roomId: Long? = null
        set(value) {
            if (field != value) {
                devices.set(emptyList())
            }
            field = value
        }
    val isLoading = ObservableBoolean()
    var isDragging = ObservableBoolean()
    val builder: ObservableField<TemplateMobileViewBuilder> = ObservableField()
    val populator: ObservableField<TemplateViewPopulator> = ObservableField(templateViewPopulator)
    val devices: ObservableField<List<Device>> = ObservableField(ArrayList())

    override fun bind() {
        super.bind()
        if (roomId == null) {
            preferences.edit().remove(CommonApplication.KEY_ROOM).remove(CommonApplication.KEY_ROOM_NAME).apply()
        } else {
            preferences.edit().set(CommonApplication.KEY_ROOM, roomId!!).set(CommonApplication.KEY_ROOM_NAME, roomName!!).apply()
        }
        if (devices.get()!!.isEmpty()) {
            builder.set(templateMobileViewBuilder)
            retrieveDevices()
        } else {
            devices.get()!!.forEach {
                it.template.reset()
            }
            builder.set(templateMobileViewBuilder)
        }
    }

    @VisibleForTesting
    fun retrieveDevices() {
        isLoading.set(true)
        deviceRepository.getDevices(roomId)
                .compose(rxErrorForwarder::catchExceptions)
                .subscribeBy(onError = {
                    isLoading.set(false)
                    Timber.e(it)
                }, onSuccess = {
                    devices.set(it.toList())
                    isLoading.set(false)
                }).addTo(subscriptions)
    }

    fun onRefresh() {
        retrieveDevices()
    }

    override fun listenForWidgetEvents(observable: Observable<WidgetEvent<Any>>) {
        observable.flatMap {
            saveValue(it).toObservable().compose(rxErrorForwarder::catchExceptions)
        }.onErrorResumeNext { _: Throwable ->
            empty()
        }.subscribeBy(onError = Timber::e)
                .addTo(subscriptions)
    }

    override fun toggleFavorite(device: Device) {
        favoriteRepository.toggleFavorite(device)
                .compose(rxErrorForwarder::catchExceptions)
                .subscribeBy(onError = {
                    Timber.e(it)
                    devices.notifyChange()
                }, onComplete = {
                    device.favorite = !device.favorite
                    devices.notifyChange()
                })
                .addTo(subscriptions)
    }

    fun saveWidgetOrder(items: List<Device>) {
        dashboardRepository.saveWidgetOrder(roomId!!, items)
                .compose(rxErrorForwarder::catchExceptions)
                .subscribeBy(onError = Timber::e)
                .addTo(subscriptions)
    }

    private fun saveValue(widgetEvent: WidgetEvent<*>): Single<Device> {
        return deviceRepository.saveWidgetValue(widgetEvent).compose(rxErrorForwarder::catchExceptions)
    }


}