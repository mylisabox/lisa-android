package com.mylisabox.lisa.common

import android.os.Bundle
import android.support.v7.app.AppCompatCallback
import android.support.v7.app.AppCompatDelegate
import android.support.v7.view.ActionMode
import android.support.wearable.activity.WearableActivity
import com.mylisabox.common.BaseViewModel
import com.mylisabox.common.CommonApplication
import com.mylisabox.lisa.LISAApplication
import com.mylisabox.lisa.dagger.components.ActivityComponent
import com.mylisabox.lisa.dagger.modules.ActivityModule
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.utils.TokenUtils
import javax.inject.Inject

abstract class BaseActivity : WearableActivity(), AppCompatCallback {
    companion object {
        val NEED_FADE_ANIMATION = "NEED_FADE_ANIMATION"
        val REQ_CODE_SPEECH_INPUT = 2
    }

    @Inject lateinit var tokenUtils: TokenUtils
    @Inject lateinit var baseNavigator: BaseNavigator
    @Inject lateinit var appPrefs: Preferences

    lateinit var activityComponent: ActivityComponent
    protected var redirectIfTokenExpired = true
    private var themeId: Int = 0
    private lateinit var delegate: AppCompatDelegate

    override fun onPause() {
        super.onPause()
        getViewModel()?.unbind()
    }

    abstract fun getViewModel(): BaseViewModel?

    override fun onResume() {
        super.onResume()
        getViewModel()?.bind()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        delegate = AppCompatDelegate.create(this, this)
        if (intent.hasExtra(NEED_FADE_ANIMATION)) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        activityComponent = (application as LISAApplication).appComponent.plusActivityComponent(
                getActivityModule()
        )
        activityComponent.inject(this)

        AppCompatDelegate.setDefaultNightMode(appPrefs.get(CommonApplication.KEY_THEME, AppCompatDelegate.MODE_NIGHT_AUTO)!!)
        super.onCreate(savedInstanceState)

        if (!isLogged() && redirectIfTokenExpired) {
            baseNavigator.goToLogin()
            finish()
        }
    }

    private fun isLogged(): Boolean {
        val token: String? = appPrefs.get(Preferences.KEY_TOKEN)
        if (!tokenUtils.isTokenExpired(token)) {
            return true
        }
        return false
    }

    private fun getActivityModule(): ActivityModule {
        return ActivityModule(this)
    }

    override fun setTheme(resid: Int) {
        super.setTheme(resid)
        // Keep hold of the theme id so that we can re-set it later if needed
        themeId = resid
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        delegate.onSaveInstanceState(outState)
    }

    override fun onWindowStartingSupportActionMode(callback: ActionMode.Callback?): ActionMode? {
        return null
    }

    override fun onSupportActionModeStarted(mode: ActionMode?) {

    }

    override fun onSupportActionModeFinished(mode: ActionMode?) {

    }

}