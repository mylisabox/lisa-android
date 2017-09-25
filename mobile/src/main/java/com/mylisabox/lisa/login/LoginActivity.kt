package com.mylisabox.lisa.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.network.dagger.annotations.ActivityScope

@ActivityScope
class LoginActivity : BaseActivity() {
    companion object {
        private val MODE_LOGIN = "login"
        val KEY_MODE = "mode"
        val MODE_REGISTER = "register"

        fun newInstance(context: Context, mode: String = MODE_LOGIN): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(BaseActivity.NEED_FADE_ANIMATION, true)
            intent.putExtra(KEY_MODE, mode)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        redirectIfTokenExpired = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_container)
        replaceFragment(LoginFragment.newInstance(intent.getStringExtra(KEY_MODE)), false)
    }

}
