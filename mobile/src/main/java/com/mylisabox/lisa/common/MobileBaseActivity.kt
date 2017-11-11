package com.mylisabox.lisa.common

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.mylisabox.common.CommonApplication
import com.mylisabox.common.chatbot.ChatBotRepository
import com.mylisabox.lisa.LISAApplication
import com.mylisabox.lisa.R
import com.mylisabox.lisa.dagger.components.ActivityComponent
import com.mylisabox.lisa.dagger.modules.ActivityModule
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.utils.RxErrorForwarder
import com.mylisabox.network.utils.TokenUtils
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.*
import javax.inject.Inject

abstract class MobileBaseActivity : AppCompatActivity() {
    companion object {
        val NEED_FADE_ANIMATION = "NEED_FADE_ANIMATION"
        val REQ_CODE_SPEECH_INPUT = 2

    }

    lateinit var activityComponent: ActivityComponent
    @Inject
    lateinit var tokenUtils: TokenUtils
    @Inject
    lateinit var baseNavigator: BaseNavigator
    @Inject
    lateinit var appPrefs: Preferences
    @Inject
    lateinit var chatBotRepository: ChatBotRepository
    @Inject
    lateinit var rxErrorForwarder: RxErrorForwarder
    protected var redirectIfTokenExpired = true
    protected lateinit var toolbar: Toolbar
    private var speechLaunched: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.hasExtra(NEED_FADE_ANIMATION)) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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

    fun showLoading() {
        findViewById<ProgressBar>(R.id.progress_spinner).visibility = View.VISIBLE
    }

    fun hideLoading() {
        findViewById<ProgressBar>(R.id.progress_spinner).visibility = View.GONE
    }

    private fun getActivityModule(): ActivityModule {
        return ActivityModule(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    protected fun isLogged(): Boolean {
        val token: String? = appPrefs.get(Preferences.KEY_TOKEN)
        if (!tokenUtils.isTokenExpired(token)) {
            return true
        }
        return false
    }

    protected fun integrateToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    fun getCurrentFragment(): Fragment {
        return supportFragmentManager.findFragmentById(R.id.container)
    }

    fun replaceFragment(newFragment: Fragment, addToBackStack: Boolean = true, tag: String = newFragment.javaClass.simpleName) {
        replaceFragment(newFragment, addToBackStack, addToBackStack, tag)
    }

    fun replaceFragment(newFragment: Fragment, addToBackStack: Boolean, animate: Boolean, tag: String = newFragment.javaClass.simpleName) {
        val findFragmentByTag: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        if (findFragmentByTag == null) {
            val ft = supportFragmentManager.beginTransaction()
            if (animate) {
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_right)
            }

            ft.replace(R.id.container, newFragment, tag)

            if (addToBackStack) {
                ft.addToBackStack(tag)
            }

            ft.commitAllowingStateLoss()
        } else if (!findFragmentByTag.isVisible) {
            supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            MobileBaseActivity.REQ_CODE_SPEECH_INPUT -> {
                speechLaunched = false
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    chatBotRepository.interact(result[0])
                            .compose(rxErrorForwarder::catchExceptions)
                            .subscribeBy(onError = Timber::e, onSuccess = {
                                Timber.d(it.toString())
                                baseNavigator.showToast(it.response)
                            })
                }
            }
        }
    }

    fun goToSpeech() {
        if (!speechLaunched) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    getString(R.string.speech_prompt))
            try {
                speechLaunched = true
                startActivityForResult(intent, MobileBaseActivity.REQ_CODE_SPEECH_INPUT)
            } catch (a: ActivityNotFoundException) {
                speechLaunched = false
                Toast.makeText(applicationContext,
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show()
            }
        }
    }
}