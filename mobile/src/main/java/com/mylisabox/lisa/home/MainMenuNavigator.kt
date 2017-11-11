package com.mylisabox.lisa.home

import android.content.Intent
import com.mylisabox.lisa.common.BaseNavigator
import com.mylisabox.lisa.common.MobileBaseActivity
import com.mylisabox.lisa.device.fragments.FavoritesFragment
import com.mylisabox.lisa.device.fragments.NewDevicesFragment
import com.mylisabox.lisa.device.fragments.RoomFragment
import com.mylisabox.lisa.profile.ProfileActivity
import com.mylisabox.lisa.settings.SettingsDialogFragment
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import com.mylisabox.network.preferences.Preferences
import javax.inject.Inject

class MainMenuNavigator @Inject constructor(@ForActivity private val activity: MobileBaseActivity,
                                            val preferences: Preferences) : BaseNavigator(activity) {
    fun reload() {
        val intent = activity.intent
        intent.putExtra(MobileBaseActivity.NEED_FADE_ANIMATION, true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        activity.finish()
        activity.startActivity(intent)
    }

    private fun closeDrawer() {
        if (activity is HomeActivity) {
            activity.closeDrawer()
        }
    }

    fun goToFavorites() {
        closeDrawer()
        activity.replaceFragment(FavoritesFragment.newInstance())
    }

    fun goToRoom(roomId: Long, roomName: String) {
        closeDrawer()
        activity.replaceFragment(RoomFragment.newInstance(roomId, roomName), tag = "${RoomFragment::class.java}_$roomId")
    }

    fun goToNewDevices() {
        closeDrawer()
        activity.replaceFragment(NewDevicesFragment.newInstance())
    }

    fun goToSettings() {
        SettingsDialogFragment.newInstance().show(activity.supportFragmentManager, "settings")
    }

    fun goToProfile() {
        activity.startActivity(ProfileActivity.newInstance(activity))
    }
}