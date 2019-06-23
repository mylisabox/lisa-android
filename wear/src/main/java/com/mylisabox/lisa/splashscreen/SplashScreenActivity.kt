package com.mylisabox.lisa.splashscreen

import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.mylisabox.common.BaseViewModel
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseActivity
import javax.inject.Inject

class SplashScreenActivity : BaseActivity() {
    @Inject lateinit var splashScreenNavigator: SplashScreenNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        redirectIfTokenExpired = false
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)
        activityComponent.inject(this)

        // Enables Always-on
        setAmbientEnabled()

        Handler().postDelayed({
            if (!isFinishing) {
                splashScreenNavigator.goToHome()
            }
        }, 2000)
    }

    override fun getViewModel(): BaseViewModel? {
        return null
    }
}
