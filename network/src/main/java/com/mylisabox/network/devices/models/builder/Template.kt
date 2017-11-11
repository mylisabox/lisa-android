package com.mylisabox.network.devices.models.builder

import android.view.View
import android.view.ViewGroup

class Template(val view: View) {
    @Suppress("UNCHECKED_CAST")
    fun <T : View> findViewByName(name: String): T {
        return view.findViewWithTag(name)
    }

    fun reset() {
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view)
        }
    }
}
