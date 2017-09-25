package com.mylisabox.network.devices.models.builder

interface TemplatePopulator {
    fun populateBy(populatorVisitor: TemplatePopulatorVisitor)
}
