package com.mylisabox.lisa.device.viewmodels

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.annotation.VisibleForTesting
import com.mylisabox.common.BaseViewModel
import com.mylisabox.common.dashboard.DashboardRepository
import com.mylisabox.common.device.DeviceRepository
import com.mylisabox.common.device.FavoriteRepository
import com.mylisabox.common.device.TemplateViewPopulator
import com.mylisabox.common.device.WidgetHandler
import com.mylisabox.lisa.common.TemplateMobileViewBuilder
import com.mylisabox.network.devices.models.Device
import com.mylisabox.network.devices.models.WidgetEvent
import com.mylisabox.network.utils.RxErrorForwarder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

abstract class DeviceViewModel(private val deviceRepository: DeviceRepository,
                               private val favoriteRepository: FavoriteRepository,
                               private val dashboardRepository: DashboardRepository,
                               private val rxErrorForwarder: RxErrorForwarder,
                               templateMobileViewBuilder: TemplateMobileViewBuilder,
                               templateViewPopulator: TemplateViewPopulator) : BaseViewModel(), WidgetHandler {
    var roomId: Long = 0
    val isLoading = ObservableBoolean()
    var isDragging = ObservableBoolean()
    val builder: ObservableField<TemplateMobileViewBuilder> = ObservableField(templateMobileViewBuilder)
    val populator: ObservableField<TemplateViewPopulator> = ObservableField(templateViewPopulator)
    val devices: ObservableField<List<Device>> = ObservableField(ArrayList())

    override fun bind() {
        super.bind()
        if (devices.get().isEmpty()) {
            retrieveDevices()
        }
    }

    @VisibleForTesting
    fun retrieveDevices() {
        isLoading.set(true)
        subscriptions?.add(deviceRepository.getDevices(roomId)
                .compose(rxErrorForwarder::catchExceptions)
                .subscribeBy(onError = {
                    isLoading.set(false)
                    Timber.e(it)
                }, onSuccess = {
                    isLoading.set(false)
                    devices.set(it.toList())
                })
        )
    }

    fun onRefresh() {
        retrieveDevices()
    }

    override fun listenForWidgetEvents(observable: Observable<WidgetEvent<Any>>) {
        subscriptions?.add(observable.flatMapSingle { saveValue(it) }
                .compose(rxErrorForwarder::catchExceptions)
                .subscribeBy(onError = Timber::e))
    }

    override fun toggleFavorite(device: Device) {
        subscriptions?.add(favoriteRepository.toggleFavorite(device)
                .compose(rxErrorForwarder::catchExceptions)
                .subscribeBy(onError = Timber::e, onComplete = { retrieveDevices() }))
    }

    fun saveWidgetOrder(items: List<Device>) {
        subscriptions?.add(dashboardRepository.saveWidgetOrder(roomId, items)
                .compose(rxErrorForwarder::catchExceptions)
                .subscribeBy(onError = Timber::e))
    }

    private fun saveValue(widgetEvent: WidgetEvent<*>): Single<Device> {
        return deviceRepository.saveWidgetValue(widgetEvent).subscribeOn(Schedulers.io())
    }
}