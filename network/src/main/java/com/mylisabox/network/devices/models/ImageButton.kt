package com.mylisabox.network.devices.models

import com.mylisabox.network.devices.models.builder.Template
import com.mylisabox.network.devices.models.builder.TemplateBuilderVisitor
import com.mylisabox.network.devices.models.builder.TemplatePopulatorVisitor

class ImageButton : BaseElement() {
    companion object {
        val KEY = "value"
    }

    init {
        type = "image-button"
    }

    fun showNextStep() {
        val values = values
        this.value = findNextValue(values, value!!)
        val key = infos!![KEY] as String
        device.get()!!.data[key] = value!!
        onChange.onNext(WidgetEvent(device.get()!!, name!!, value!!))
    }

    private fun findNextValue(values: Map<String, String>?, value: String?): String {
        var nextValue: String? = null
        var found = false
        if (values == null) {
            nextValue = value
        } else {
            for (key in values.keys) {
                if (nextValue == null) {
                    nextValue = key
                }
                if (found) {
                    nextValue = key
                }
                if (key == value) {
                    found = true
                }
            }
        }
        return nextValue!!
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
