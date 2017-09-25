package com.mylisabox.lisa.splashscreen

import android.content.Intent
import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.lisa.common.BaseNavigator
import com.mylisabox.lisa.home.HomeActivity
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import javax.inject.Inject

class SplashScreenNavigator @Inject constructor(@ForActivity private val activity: BaseActivity) : BaseNavigator(activity) {

    fun goToHome() {
        val intent = Intent(activity, HomeActivity::class.java)
        intent.putExtra(BaseActivity.NEED_FADE_ANIMATION, true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        activity.finish()
    }
}