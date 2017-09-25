package com.mylisabox.lisa.common

import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes
import android.widget.Toast
import com.mylisabox.lisa.login.LoginActivity
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import javax.inject.Inject

open class BaseNavigator @Inject constructor(@ForActivity private val context: Context) {
    open fun goBack() {
        if (context is BaseActivity) {
            context.onBackPressed()
        }
    }

    fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }

    fun goToLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra(BaseActivity.NEED_FADE_ANIMATION, true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        if (context is BaseActivity) {
            context.finish()
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showToast(@StringRes message: Int) {
        showToast(context.getString(message))
    }
}