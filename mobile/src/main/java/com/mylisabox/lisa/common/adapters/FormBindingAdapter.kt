package com.mylisabox.lisa.common.adapters

import android.databinding.BindingAdapter
import android.support.annotation.StringRes
import android.widget.EditText

object FormBindingAdapter {
    @JvmStatic
    @BindingAdapter("error")
    fun setError(view: EditText, @StringRes error: Int) {
        if (error != 0) {
            view.error = view.context.getString(error)
        }
    }
}