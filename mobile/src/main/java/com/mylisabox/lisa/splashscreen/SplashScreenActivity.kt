package com.mylisabox.lisa.splashscreen

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.mylisabox.common.backend.ServerRepository
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.network.dagger.annotations.ActivityScope
import com.mylisabox.network.utils.RxErrorForwarder
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
@ActivityScope
class SplashScreenActivity : BaseActivity() {
    @Inject lateinit var splashScreenNavigator: SplashScreenNavigator
    @Inject lateinit var serverRepository: ServerRepository
    @Inject lateinit var rxErrorHandler: RxErrorForwarder

    override fun onCreate(savedInstanceState: Bundle?) {
        redirectIfTokenExpired = false
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.splash_screen_activity)
        activityComponent.inject(this)

        serverRepository.isInitialized()
                .compose(rxErrorHandler::catchExceptions)
                .subscribeBy(onError = {
                    if (!isFinishing) {
                        splashScreenNavigator.goToLogin()
                    }
                }, onSuccess = {
                    if (!isFinishing) {
                        if (it) {
                            splashScreenNavigator.goToHome()
                        } else {
                            splashScreenNavigator.goToRegistration()
                        }
                    }
                })
    }

}
