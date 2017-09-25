package com.mylisabox.common.utils

import android.widget.ImageView

interface ImagesProvider {
    fun loadImage(url: String, imageView: ImageView)
}
