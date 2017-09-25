package com.mylisabox.lisa.home

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.mylisabox.common.BaseViewModel
import com.mylisabox.common.room.RoomRepository
import com.mylisabox.lisa.R
import com.mylisabox.network.room.models.Room
import com.mylisabox.network.utils.RxErrorForwarder
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val roomRepository: RoomRepository,
                                        private val rxErrorForwarder: RxErrorForwarder,
                                        private val homeNavigator: HomeNavigator) : BaseViewModel() {
    private val rooms = ArrayList<Room>()
    val isLoading = ObservableBoolean()
    val menu = ObservableField(ArrayList<MenuItem>())

    override fun bind() {
        super.bind()
        onRefresh()
    }

    fun onRefresh() {
        isLoading.set(true)
        subscriptions?.add(roomRepository.retrieveAll()
                .compose(rxErrorForwarder::catchExceptions)
                .subscribeBy(onError = {
                    Timber.e(it)
                    isLoading.set(false)
                }, onSuccess = {
                    rooms.clear()
                    rooms.addAll(it)
                    buildMenu()
                    isLoading.set(false)
                }))
    }

    private fun buildMenu() {
        menu.get().add(MenuItem(R.drawable.ic_mic_black_24dp, homeNavigator.getString(R.string.talk_to_lisa)))
        menu.get().add(MenuItem(R.drawable.ic_favorite_24dp, homeNavigator.getString(R.string.menu_favorites)))
        rooms.mapTo(menu.get()) { MenuItem(R.drawable.ic_home_24dp, it.name) }
        menu.get().add(MenuItem(R.drawable.ic_tune_24dp, homeNavigator.getString(R.string.menu_new_devices)))
    }
}
