package com.mylisabox.network.devices.models.builder

interface TemplateBuilder {
    fun buildView(templateBuilderVisitor: TemplateBuilderVisitor): Template
}
