package com.mylisabox.lisa.utils.image

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.util.Log

import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.caverock.androidsvg.SVG
import com.mylisabox.lisa.BuildConfig
import com.mylisabox.lisa.R

import java.io.InputStream

/**
 * Module for the SVG sample app.
 */
@GlideModule
class SvgModule : AppGlideModule() {
    private val options = RequestOptions().fitCenter().error(R.drawable.ic_lisa_black)

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        if (BuildConfig.DEBUG) {
            builder.setLogLevel(Log.VERBOSE)
        }
        builder.setDefaultRequestOptions(options)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.register(SVG::class.java, PictureDrawable::class.java, SvgDrawableTranscoder())
                .append(InputStream::class.java, SVG::class.java, SvgDecoder())
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
