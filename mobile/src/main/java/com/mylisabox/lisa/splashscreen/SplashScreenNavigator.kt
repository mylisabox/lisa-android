package com.mylisabox.lisa.splashscreen

import android.content.Intent
import com.mylisabox.lisa.common.BaseNavigator
import com.mylisabox.lisa.common.MobileBaseActivity
import com.mylisabox.lisa.common.MobileBaseActivity.Companion.NEED_FADE_ANIMATION
import com.mylisabox.lisa.home.HomeActivity
import com.mylisabox.network.dagger.annotations.ActivityScope
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import javax.inject.Inject

@ActivityScope
class SplashScreenNavigator @Inject constructor(@ForActivity private val activity: MobileBaseActivity) : BaseNavigator(activity) {

    fun goToHome() {
        val intent = Intent(activity, HomeActivity::class.java)
        intent.putExtra(NEED_FADE_ANIMATION, true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        activity.finish()
    }
}