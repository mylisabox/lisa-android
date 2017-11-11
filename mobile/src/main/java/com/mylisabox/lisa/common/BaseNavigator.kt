package com.mylisabox.lisa.common

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.mylisabox.lisa.R
import com.mylisabox.lisa.login.LoginActivity
import com.mylisabox.lisa.login.LoginActivity.Companion.MODE_REGISTER
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import com.mylisabox.network.utils.RxErrorForwarder.LoginNavigation
import javax.inject.Inject

open class BaseNavigator @Inject constructor(@ForActivity private val context: Context) : LoginNavigation {
    private var progressDialog: ProgressDialog? = null
    protected var progressDialogVisible = false
    private val progressDialogHandler = Handler()

    open fun goBack() {
        if (context is MobileBaseActivity) {
            context.onBackPressed()
        }
    }

    override fun goToLogin() {
        context.startActivity(LoginActivity.newInstance(context))
        if (context is MobileBaseActivity) {
            context.finish()
        }
    }

    fun goToRegistration() {
        context.startActivity(LoginActivity.newInstance(context, MODE_REGISTER))
        if (context is MobileBaseActivity) {
            context.finish()
        }
    }

    fun showLoading(title: Int = R.string.title_wait, message: Int = R.string.please_wait) {
        showLoading(context.getString(title), context.getString(message))
    }

    fun showLoading(title: String, message: String): ProgressDialog {
        if (!progressDialogVisible) {
            progressDialogVisible = true
            progressDialog = ProgressDialog(context)
            progressDialog?.setTitle(title)
            progressDialog?.setMessage(message)
            progressDialogHandler.postDelayed({ progressDialog?.show() }, 300)
        }
        return progressDialog!!
    }

    fun hideLoading() {
        progressDialogHandler.removeCallbacksAndMessages(null)
        progressDialog?.hide()
        progressDialogVisible = false
    }

    fun showMessage(title: String, message: String): Dialog {
        return AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .show()
    }

    fun showConfirm(title: String, message: String,
                    positiveButton: String = context.getString(android.R.string.ok),
                    negativeButton: String = context.getString(android.R.string.cancel),
                    onPositiveClickListener: DialogInterface.OnClickListener? = null,
                    onNegativeClickListener: DialogInterface.OnClickListener? = null): Dialog {
        return AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setOnDismissListener { onNegativeClickListener?.onClick(it, 0) }
                .setPositiveButton(positiveButton, onPositiveClickListener)
                .setNegativeButton(negativeButton, onNegativeClickListener)
                .show()
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showToast(@StringRes message: Int) {
        showToast(context.getString(message))
    }
}