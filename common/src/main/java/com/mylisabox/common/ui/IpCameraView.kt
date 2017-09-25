package com.mylisabox.common.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import com.mylisabox.common.BuildConfig.WEBCAM_SNAPSHOT_BASE_PATH
import com.mylisabox.common.BuildConfig.WEBCAM_STREAM_BASE_PATH
import com.mylisabox.common.R
import com.mylisabox.network.utils.BaseUrlProvider

@SuppressLint("ViewConstructor")
class IpCameraView(context: Context, private val baseUrlProvider: BaseUrlProvider) : LinearLayout(context) {
    private val webView: WebView
    private val btPlayPause: ImageView
    private val btRefresh: ImageView
    private val btFullScreen: ImageView
    private var image: String? = null
    private var video: String? = null
    private var isModeSnapshot = true

    private var modeSnapshot: Boolean
        set(value) {
            isModeSnapshot = value
            setupUi()
            loadUrl()
        }
        get() = isModeSnapshot

    var imageUrl: String?
        set(value) {
            image = value
            loadUrl()
        }
        get() = if (image == null) video else image

    var videoUrl: String?
        set(value) {
            video = value
            loadUrl()
        }
        get() = video

    init {
        orientation = HORIZONTAL
        addView(LayoutInflater.from(context).inflate(R.layout.view_camera, this, false))
        btFullScreen = findViewById(R.id.fullscreen)
        btPlayPause = findViewById(R.id.playpause)
        btRefresh = findViewById(R.id.refresh)

        btRefresh.setOnClickListener { loadUrl() }
        btPlayPause.setOnClickListener { modeSnapshot = !modeSnapshot }
        btFullScreen.setOnClickListener {
            //TODO create full screen mode
        }

        webView = findViewById(R.id.webview)
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
    }

    private fun setupUi() {
        if (isModeSnapshot) {
            btRefresh.visibility = View.VISIBLE
            btPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        } else {
            btRefresh.visibility = View.GONE
            btPlayPause.setImageResource(R.drawable.ic_pause_black_24dp)
        }
    }

    private fun loadUrl() {
        val path = String.format(if (isModeSnapshot) WEBCAM_SNAPSHOT_BASE_PATH else WEBCAM_STREAM_BASE_PATH,
                baseUrlProvider.getToken(), if (isModeSnapshot) imageUrl else videoUrl)
        webView.loadData("<html>" +
                "<head><style>html, body{padding:0;margin:0; text-align: center; background:transparent;} img{height: 100%}</style></head>" +
                "<body><img src='${baseUrlProvider.getBaseUrl()}$path'/></body>" +
                "</html>", "text/html", "UTF8")
    }
}