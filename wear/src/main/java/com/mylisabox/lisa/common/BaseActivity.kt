package com.mylisabox.lisa.common

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v7.app.AppCompatCallback
import android.support.v7.app.AppCompatDelegate
import android.support.v7.view.ActionMode
import android.support.wearable.activity.WearableActivity
import android.widget.Toast
import com.mylisabox.common.BaseViewModel
import com.mylisabox.common.CommonApplication
import com.mylisabox.common.CommonApplication.Companion.WEAR_SPEECH_PATH
import com.mylisabox.common.wearable.Wearable
import com.mylisabox.lisa.LISAApplication
import com.mylisabox.lisa.R
import com.mylisabox.lisa.dagger.components.ActivityComponent
import com.mylisabox.lisa.dagger.modules.ActivityModule
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.utils.TokenUtils
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.*
import javax.inject.Inject

abstract class BaseActivity : WearableActivity(), AppCompatCallback {
    companion object {
        val NEED_FADE_ANIMATION = "NEED_FADE_ANIMATION"
        val REQ_CODE_SPEECH_INPUT = 2
    }

    @Inject lateinit var tokenUtils: TokenUtils
    @Inject lateinit var baseNavigator: BaseNavigator
    @Inject lateinit var appPrefs: Preferences
    @Inject lateinit var wearable: Wearable

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
        //FIXME see a way to do this set as logged for now
        val token: String? = appPrefs.get(Preferences.KEY_TOKEN)
        if (!tokenUtils.isTokenExpired(token)) {
            return true
        }
        return true
    }

    fun goToSpeech() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt))
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(applicationContext, getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show()
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    wearable.sendMessage(WEAR_SPEECH_PATH, result[0])
                            .subscribeBy(onError = {
                                Timber.e(it)
                            })
                }
            }
        }
    }

    override fun onWindowStartingSupportActionMode(callback: ActionMode.Callback?): ActionMode? {
        return null
    }

    override fun onSupportActionModeStarted(mode: ActionMode?) {

    }

    override fun onSupportActionModeFinished(mode: ActionMode?) {

    }

}