package com.mylisabox.lisa.home

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.jaumard.recyclerviewbinding.BindableRecyclerAdapter
import com.mylisabox.common.BaseViewModel
import com.mylisabox.lisa.R
import com.mylisabox.lisa.room.RoomsWearRepository
import com.mylisabox.network.room.models.Room
import com.mylisabox.network.utils.RxErrorForwarder
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val roomRepository: RoomsWearRepository,
                                        private val rxErrorForwarder: RxErrorForwarder,
                                        private val homeNavigator: HomeNavigator) : BaseViewModel(), BindableRecyclerAdapter.OnClickListener<MenuItem> {
    companion object {
        const val MENU_SPEECH = -1L
        const val MENU_FAVORITES = -2L
        const val MENU_NEW_DEVICES = -3L
    }

    private val rooms = ArrayList<Room>()
    val isLoading = ObservableBoolean()
    val menu = ObservableField(ArrayList<MenuItem>())

    override fun bind() {
        super.bind()
        if (menu.get()!!.isEmpty()) {
            onRefresh()
        }
    }

    override fun onClick(item: MenuItem) {
        when (item.id) {
            MENU_SPEECH -> {
                homeNavigator.goToSpeech()
            }
            MENU_FAVORITES -> {
                homeNavigator.goToFavorites()
            }
            MENU_NEW_DEVICES -> {
                homeNavigator.goToNewDevices()
            }
            else -> {
                homeNavigator.goToRoom(item.id)
            }
        }
    }

    fun onRefresh() {
        isLoading.set(true)

        subscriptions.add(roomRepository.retrieveAll()
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
        val newMenu = ArrayList<MenuItem>()
        newMenu.add(MenuItem(MENU_SPEECH, R.drawable.ic_mic_black_24dp, homeNavigator.getString(R.string.talk_to_lisa)))
        newMenu.add(MenuItem(MENU_FAVORITES, R.drawable.ic_favorite_24dp, homeNavigator.getString(R.string.menu_favorites)))
        rooms.mapTo(newMenu) { MenuItem(it.id!!, R.drawable.ic_home_24dp, it.name) }
        newMenu.add(MenuItem(MENU_NEW_DEVICES, R.drawable.ic_tune_24dp, homeNavigator.getString(R.string.menu_new_devices)))
        menu.set(newMenu)
    }
}
