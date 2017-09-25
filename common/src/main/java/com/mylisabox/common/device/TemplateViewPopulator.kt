package com.mylisabox.common.device

import android.graphics.Color
import android.text.TextUtils
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Switch
import com.mylisabox.common.ui.ColorPickerPreView
import com.mylisabox.common.ui.IpCameraView

import com.mylisabox.common.utils.ImagesProvider
import com.mylisabox.network.devices.models.*
import com.mylisabox.network.devices.models.builder.TemplatePopulatorVisitor
import javax.inject.Inject

class TemplateViewPopulator @Inject constructor(private var imagesProvider: ImagesProvider) : TemplatePopulatorVisitor {

    override fun populateComponent(component: Space) {

    }

    override fun populateComponent(component: ToggleButton) {
        val value = component.device.get()!!.data[component.infos!!["value"]] as Boolean
        val toggle = component.template!!.findViewByName<Switch>(component.name!!)
        toggle.isChecked = value
    }

    override fun populateComponent(component: VBox) {
        for (baseElement in component.children) {
            baseElement.populateBy(this)
        }
    }

    override fun populateComponent(component: HBox) {
        for (baseElement in component.children) {
            baseElement.populateBy(this)
        }
    }

    override fun populateComponent(component: Slider) {
        val key = component.infos!![Slider.KEY] as String
        val value = component.device.get()!!.data[key]
        val text: Double? = (value as? String)?.toDouble() ?: value as Double

        val slider = component.template!!.findViewByName<SeekBar>(component.name!!)
        slider.progress = text?.toInt() ?: 0
    }

    override fun populateComponent(component: Image) {
        throw RuntimeException("Not implemented yet")
    }

    override fun populateComponent(component: ImageButton) {
        val values = component.infos!!["values"]
        if (values is Map<*, *>) {
            component.values = values as Map<String, String>?
        } else {
            component.values = component.device.get()!!.data[values] as? Map<String, String>
        }
        val key = component.infos!![ImageButton.KEY] as String
        if (key.contains("/")) {
            component.value = key
        } else {
            component.value = component.device.get()!!.data[key] as? String
        }

        val imageButton = component.template!!.findViewByName<ImageView>(component.name!!)

        imageButton.setOnClickListener { _ ->
            component.showNextStep()
            imagesProvider.loadImage(if (component.values == null) component.value!! else component.values!![component.value]!!, imageButton)
        }
        imagesProvider.loadImage(if (component.values == null) component.value!! else component.values!![component.value]!!, imageButton)
    }

    override fun populateComponent(component: ColorPicker) {
        val colorPicker = component.template!!.findViewByName<ColorPickerPreView>(component.name!!)
        colorPicker.color = component.color
        colorPicker.setBackgroundColor(Color.RED)
    }

    override fun populateComponent(component: Card) {
        for (baseElement in component.children) {
            baseElement.populateBy(this)
        }
    }

    override fun populateComponent(component: Camera) {
        val key = component.infos!!["video"] as String
        var url = component.device.get()!!.data[key] as String
        if (!TextUtils.isEmpty(url)) {
            component.template?.findViewByName<IpCameraView>(component.name!!)?.videoUrl = url
        }
    }

    override fun populateComponent(component: Button) {
        val key = component.infos!!["text"] as String
        var text = component.device.get()!!.data[key] as? String
        val button = component.template!!.findViewByName<android.widget.Button>(component.name!!)
        if (text == null && key.contains(".")) {
            val parts = key.split(".")
            var subData = component.device.get()!!.data
            for (part in parts) {
                if (subData[part] is Map<*, *>) {
                    subData = subData[part] as MutableMap<String, Any>
                } else {
                    text = subData[part] as? String
                }
            }
            if (text == null) {
                text = ""
            }
        }
        button.text = text ?: key
    }
}
