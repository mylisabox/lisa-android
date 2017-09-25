package com.mylisabox.lisa.common.adapters

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.mylisabox.lisa.R
import com.mylisabox.lisa.utils.image.GlideApp

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("roundedImage")
    fun setRoundedImage(view: ImageView, url: String?) {
        if (url == null || url.isBlank() || url.isEmpty()) {
            view.setImageResource(R.mipmap.ic_launcher)
        } else {
            GlideApp.with(view.context)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUrl(view: ImageView, url: String?) {
        if (url == null || url.isBlank() || url.isEmpty()) {
            view.setImageResource(R.mipmap.ic_launcher)
        } else {
            GlideApp.with(view.context).load(url).into(view)
        }
    }
}