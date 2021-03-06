package com.mylisabox.network.devices.models

import com.mylisabox.network.devices.models.builder.Template
import com.mylisabox.network.devices.models.builder.TemplateBuilderVisitor
import com.mylisabox.network.devices.models.builder.TemplatePopulatorVisitor

class ToggleButton : BaseElement() {
    init {
        type = "toggle-button"
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
