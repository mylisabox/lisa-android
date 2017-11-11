package com.mylisabox.lisa.common

import android.databinding.BindingAdapter
import android.widget.ImageView

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("src")
    fun setSrc(view: ImageView, drawable: Int) {
        view.setImageResource(drawable)
    }
}