package com.mylisabox.lisa.login.adapters

import android.databinding.BindingAdapter
import android.support.design.widget.TextInputEditText
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.mylisabox.lisa.utils.OnImeClicked

object LoginBindingAdapter {
    @JvmStatic
    @BindingAdapter("onImeClicked")
    fun setPasswordImeClicked(editText: TextInputEditText, consumer: OnImeClicked) {
        editText.setOnEditorActionListener { _, actionId, event ->
            run {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.action == KeyEvent.ACTION_DOWN
                        && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                    consumer.onImeClicked()
                    return@run true
                }
                return@run false
            }
        }
    }
}