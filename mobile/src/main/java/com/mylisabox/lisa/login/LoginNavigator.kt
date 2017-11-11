package com.mylisabox.lisa.login

import com.mylisabox.lisa.common.BaseNavigator
import com.mylisabox.lisa.common.MobileBaseActivity
import com.mylisabox.lisa.home.HomeActivity
import com.mylisabox.lisa.settings.SettingsDialogFragment
import com.mylisabox.network.dagger.annotations.FragmentScope
import com.mylisabox.network.dagger.annotations.Qualifiers.ForFragment
import javax.inject.Inject

@FragmentScope
class LoginNavigator @Inject constructor(@ForFragment private val activity: MobileBaseActivity) : BaseNavigator(activity) {

    fun goToHome() {
        val intent = HomeActivity.newInstance(activity)
        intent.putExtra(MobileBaseActivity.NEED_FADE_ANIMATION, true)
        activity.startActivity(intent)
        activity.finish()
    }

    fun goToSettings() {
        SettingsDialogFragment.newInstance().show(activity.supportFragmentManager, "settings")
    }
}