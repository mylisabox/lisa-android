package com.mylisabox.lisa.utils

import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.mylisabox.common.utils.ImagesProvider
import com.mylisabox.lisa.utils.image.GlideApp
import com.mylisabox.lisa.utils.image.SvgSoftwareLayerSetter
import com.mylisabox.network.utils.BaseUrlProvider
import javax.inject.Inject

class MobileImagesProvider @Inject constructor(private val baseUrlProvider: BaseUrlProvider) : ImagesProvider {
    private var requestBuilder: RequestBuilder<PictureDrawable>? = null

    override fun loadImage(url: String, imageView: ImageView) {
        val fullPath = "${baseUrlProvider.getBaseUrl()}$url"

        if (requestBuilder == null) {
            requestBuilder = GlideApp
                    .with(imageView)
                    .`as`(PictureDrawable::class.java)
                    .transition(com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade())
                    .listener(SvgSoftwareLayerSetter())
        }



        if (fullPath.endsWith(".svg", true)) {
            requestBuilder!!.load(fullPath).into(imageView)
        } else {
            GlideApp.with(imageView)
                    .asBitmap()
                    .load(fullPath)
                    .transition(withCrossFade())
                    .into(imageView)
        }
    }
}