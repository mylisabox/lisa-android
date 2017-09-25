package com.mylisabox.network.devices.models.builder

import com.mylisabox.network.devices.models.*

interface TemplateBuilderVisitor {
    fun build(component: Space): Template

    fun build(component: ToggleButton): Template

    fun build(component: VBox): Template

    fun build(component: HBox): Template

    fun build(component: Slider): Template

    fun build(component: Image): Template

    fun build(component: ImageButton): Template

    fun build(component: ColorPicker): Template

    fun build(component: Card): Template

    fun build(component: Camera): Template

    fun build(component: Button): Template

    //fun getLastLayoutParams(): ViewGroup.LayoutParams
}