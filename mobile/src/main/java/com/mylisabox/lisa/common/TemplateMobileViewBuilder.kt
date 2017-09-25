package com.mylisabox.lisa.common

import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.jrummyapps.android.colorpicker.ColorPickerView
import com.mylisabox.common.device.BaseTemplateBuilderVisitor
import com.mylisabox.common.ui.ColorPickerPreView
import com.mylisabox.common.utils.DimensionUtils
import com.mylisabox.lisa.device.ui.ColorPickerDialog
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import com.mylisabox.network.devices.models.ColorPicker
import com.mylisabox.network.devices.models.WidgetEvent
import com.mylisabox.network.devices.models.builder.Template
import com.mylisabox.network.utils.BaseUrlProvider
import javax.inject.Inject

class TemplateMobileViewBuilder @Inject constructor(@ForActivity context: Context,
                                                    baseUrlProvider: BaseUrlProvider,
                                                    private val dimensionUtils: DimensionUtils) :
        BaseTemplateBuilderVisitor(context, baseUrlProvider, LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)) {

    override fun build(component: ColorPicker): Template {
        val container = RelativeLayout(context)
        container.layoutParams = lastLayoutParams

        val colorPicker = ColorPickerPreView(context)
        colorPicker.tag = component.name
        colorPicker.setOnClickListener { v ->
            val colorPickerDialog = ColorPickerDialog(v.context)
            colorPickerDialog.onColorChangedListener = ColorPickerView.OnColorChangedListener { newColor ->
                colorPicker.color = newColor
                val key = component.infos!!["value"] as String
                val hexColor = String.format("#%06X", 0xFFFFFF and newColor)
                component.device.get()?.data?.put(key, hexColor)
                component.onChange.onNext(WidgetEvent(component.device.get()!!, component.name!!, hexColor))
            }
            colorPickerDialog.show()
            colorPickerDialog.setOldColor(component.color)
            colorPickerDialog.setNewColor(component.color)
        }
        val layoutParams = RelativeLayout.LayoutParams(dimensionUtils.convertDpToPixel(35f).toInt(), dimensionUtils.convertDpToPixel(35f).toInt())
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        colorPicker.layoutParams = layoutParams
        container.addView(colorPicker)
        return Template(container)
    }
}