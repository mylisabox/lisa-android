package com.mylisabox.lisa.home

import android.databinding.*
import android.support.v7.app.AppCompatDelegate
import android.text.TextUtils
import com.mylisabox.common.BaseViewModel
import com.mylisabox.common.CommonApplication
import com.mylisabox.common.room.RoomRepository
import com.mylisabox.common.user.UserRepository
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.room.models.Room
import com.mylisabox.network.user.models.User
import com.mylisabox.network.utils.BaseUrlProvider
import com.mylisabox.network.utils.RxErrorForwarder
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class MainMenuViewModel @Inject constructor(private val appPrefs: Preferences,
                                            private val baseUrlProvider: BaseUrlProvider,
                                            private val userRepository: UserRepository,
                                            private val roomRepository: RoomRepository,
                                            private val mainMenuNavigator: MainMenuNavigator,
                                            private val rxErrorForwarder: RxErrorForwarder) : BaseViewModel() {

    val avatar: ObservableField<String> = ObservableField()
    val user: ObservableField<User> = ObservableField()
    val rooms: ObservableList<Room> = ObservableArrayList()
    val isNightTheme: ObservableBoolean = ObservableBoolean()
    val isRoomsOpen: ObservableBoolean = ObservableBoolean()
    val isLoading: ObservableBoolean = ObservableBoolean()
    val roomListHeight: ObservableInt = ObservableInt()
    val newRoomName: ObservableField<String> = ObservableField()
    val userName: ObservableField<String> = ObservableField()
    val onRoomListener: OnRoomListener = OnRoomListener(this)
    private val onNightThemeListener = OnNightThemeChangedCallback(this)

    init {
        user.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(p0: Observable?, p1: Int) {
                userName.set(if (TextUtils.isEmpty(user.get()!!.firstName)) user.get()!!.email else user.get()!!.firstName)
                var avatarUrl = user.get()!!.avatar
                if (avatarUrl != null && avatarUrl.startsWith("/")) {
                    avatarUrl = baseUrlProvider.getBaseUrl() + avatarUrl
                }
                avatar.set(avatarUrl)
            }

        })
    }

    fun goToProfile() {
        mainMenuNavigator.goToProfile()
    }

    fun goToFavorites() {
        mainMenuNavigator.goToFavorites()
    }

    fun goToNewDevices() {
        mainMenuNavigator.goToNewDevices()
    }

    fun goToSettings() {
        mainMenuNavigator.goToSettings()
    }

    private fun switchTheme(isNightMode: Boolean) {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        val themeId = if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        appPrefs.setAndApply(CommonApplication.KEY_THEME, themeId)
        mainMenuNavigator.reload()
    }

    fun onRoomClicked(roomId: Long, roomName: String) {
        mainMenuNavigator.goToRoom(roomId, roomName)
    }

    fun onRoomCreate(roomName: String) {
        mainMenuNavigator.showLoading()
        subscriptions?.add(roomRepository.create(roomName)
                .compose(rxErrorForwarder::catchExceptions)
                .subscribeBy(onSuccess = {
                    mainMenuNavigator.hideLoading()
                    newRoomName.set("")
                }, onError = {
                    Timber.e(it)
                }))
    }

    override fun bind() {
        super.bind()
        isNightTheme.addOnPropertyChangedCallback(onNightThemeListener)
        if (rooms.isEmpty()) {
            retrieveRooms()
        }
        getUserProfile()
    }

    private fun getUserProfile() {
        subscriptions?.add(userRepository.getProfile()
                .compose(rxErrorForwarder::catchExceptions)
                .subscribeBy(onError = Timber::e, onSuccess = {
                    user.set(it)
                }))
    }

    private fun retrieveRooms() {
        isLoading.set(true)
        subscriptions?.add(
                roomRepository.retrieveAll()
                        .compose(rxErrorForwarder::catchExceptions)
                        .subscribeBy(onError = {
                            Timber.e(it)
                            isLoading.set(false)
                        }, onSuccess = {
                            this.rooms.clear()
                            this.rooms.addAll(it)
                            calculateRoomListHeight()
                            isLoading.set(false)
                        }))
    }

    fun onRefresh() {
        retrieveRooms()
    }

    override fun unbind() {
        super.unbind()
        isNightTheme.removeOnPropertyChangedCallback(onNightThemeListener)
    }

    private fun calculateRoomListHeight() {
        roomListHeight.set(rooms.size + 1)
    }

    fun toggleRooms() {
        isRoomsOpen.set(!isRoomsOpen.get())
    }

    fun logout() {
        userRepository.logoutUser()
                .compose(rxErrorForwarder::catchExceptions)
                .doOnComplete {
                    appPrefs.removeAndApply(Preferences.KEY_TOKEN)
                    mainMenuNavigator.goToLogin()
                }
                .subscribeBy(onError = {
                    Timber.e(it)
                    appPrefs.removeAndApply(Preferences.KEY_TOKEN)
                    mainMenuNavigator.goToLogin()
                })
    }

    private class OnNightThemeChangedCallback(private val mainMenuViewModel: MainMenuViewModel) : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable, i: Int) {
            this.mainMenuViewModel.switchTheme((observable as ObservableBoolean).get())
        }
    }

    class OnRoomListener internal constructor(private val mainViewModel: MainMenuViewModel) {

        fun onRoomClicked(roomId: Long, roomName: String) {
            mainViewModel.onRoomClicked(roomId, roomName)
        }

        fun onRoomCreate(roomName: String) {
            mainViewModel.onRoomCreate(roomName)
        }
    }
}