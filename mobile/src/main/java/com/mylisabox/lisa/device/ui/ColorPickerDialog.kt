package com.mylisabox.lisa.device.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.StyleRes
import android.view.View
import android.view.Window

import com.jrummyapps.android.colorpicker.ColorPanelView
import com.jrummyapps.android.colorpicker.ColorPickerView
import com.mylisabox.lisa.R

class ColorPickerDialog : Dialog {
    var onColorChangedListener: ColorPickerView.OnColorChangedListener? = null
    private var panelNew: ColorPanelView? = null
    private var panelOld: ColorPanelView? = null
    private var colorPickerView: ColorPickerView? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId) {}

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.color_picker_dialog)

        findViewById<View>(R.id.cancelButton).setOnClickListener({ _ -> cancel() })
        findViewById<View>(R.id.okButton).setOnClickListener({ _ -> validate() })
        colorPickerView = findViewById(R.id.cpv_color_picker_view)
        panelNew = findViewById(R.id.cpv_color_panel_new)
        panelOld = findViewById(R.id.cpv_color_panel_old)

        colorPickerView!!.setAlphaSliderVisible(false)
        colorPickerView!!.setOnColorChangedListener { _ -> panelNew!!.color = colorPickerView!!.color }
    }

    fun setOldColor(color: Int) {
        panelOld!!.color = color
        colorPickerView!!.color = color
    }

    private fun validate() {
        if (onColorChangedListener != null) {
            onColorChangedListener!!.onColorChanged(panelNew!!.color)
        }
        dismiss()
    }

    fun setNewColor(color: Int) {
        panelNew!!.color = color
    }
}
