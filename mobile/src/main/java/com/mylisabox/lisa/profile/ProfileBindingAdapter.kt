package com.mylisabox.lisa.profile

import android.databinding.BindingAdapter
import android.net.Uri
import android.widget.ImageView
import com.mylisabox.lisa.common.adapters.ImageBindingAdapter
import java.io.File

object ProfileBindingAdapter {
    @JvmStatic
    @BindingAdapter("roundedImage", "avatar")
    fun setRoundedImageOrAvatar(view: ImageView, url: String?, avatar: File?) {
        if (avatar != null) {
            view.setImageURI(Uri.fromFile(avatar))
        } else {
            ImageBindingAdapter.setRoundedImage(view, url)
        }
    }
}