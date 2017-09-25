package com.mylisabox.network.devices.models.builder

import android.view.View

class Template(val view: View) {
    @Suppress("UNCHECKED_CAST")
    fun <T : View> findViewByName(name: String): T {
        return view.findViewWithTag(name)
    }
}
