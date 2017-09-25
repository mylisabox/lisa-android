package com.mylisabox.network.devices.models.builder

import com.mylisabox.network.devices.models.*

interface TemplatePopulatorVisitor {
    fun populateComponent(component: Space)

    fun populateComponent(component: ToggleButton)

    fun populateComponent(component: VBox)

    fun populateComponent(component: HBox)

    fun populateComponent(component: Slider)

    fun populateComponent(component: Image)

    fun populateComponent(component: ImageButton)

    fun populateComponent(component: ColorPicker)

    fun populateComponent(component: Card)

    fun populateComponent(component: Camera)

    fun populateComponent(component: Button)

}
