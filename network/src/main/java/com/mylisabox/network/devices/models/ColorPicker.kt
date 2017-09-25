package com.mylisabox.network.devices.models

import android.graphics.Color

import com.mylisabox.network.devices.models.builder.Template
import com.mylisabox.network.devices.models.builder.TemplateBuilderVisitor
import com.mylisabox.network.devices.models.builder.TemplatePopulatorVisitor

class ColorPicker : BaseElement() {

    val color: Int
        get() {
            val key = infos!!["value"] as String
            val text = device.get()!!.data[key] as String?
            return Color.parseColor(text ?: "#000")
        }

    init {
        type = "color-picker"
    }

    override fun buildView(templateBuilderVisitor: TemplateBuilderVisitor): Template {
        if (template == null) {
            template = templateBuilderVisitor.build(this)
        }
        return template!!
    }

    override fun populateBy(populatorVisitor: TemplatePopulatorVisitor) {
        populatorVisitor.populateComponent(this)
    }
}
