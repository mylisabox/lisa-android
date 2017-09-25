package com.mylisabox.lisa.ui

import android.app.Dialog
import android.content.Context
import android.support.annotation.NonNull
import android.view.ViewGroup
import android.view.WindowManager
import com.mylisabox.lisa.R
import org.jraf.android.androidwearcolorpicker.view.ColorPickListener
import org.jraf.android.androidwearcolorpicker.view.ColorPickView


class ColorPickerDialog(@NonNull context: Context) : Dialog(context, R.style.AppTheme_Wear) {
    private lateinit var colorPickView: ColorPickView

    init {
        init()
    }

    private fun init() {
        setContentView(R.layout.dialog_color_picker)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        colorPickView = findViewById(R.id.color_picker)
    }

    fun setColorPickerListener(listener: ColorPickListener) {
        colorPickView.setListener(listener)
    }

    fun setOldColor(oldColor: Int) {
        colorPickView.setOldColor(oldColor)
    }
}
