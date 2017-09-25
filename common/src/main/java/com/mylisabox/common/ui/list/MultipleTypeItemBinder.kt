package com.mylisabox.common.ui.list

import android.support.annotation.LayoutRes
import android.support.v4.util.Pair
import java.util.*

abstract class MultipleTypeItemBinder {
    companion object {
        @JvmStatic
        protected val TYPE_NONE = 0
    }

    @LayoutRes
    @get:LayoutRes
    val layoutId: Int
    val variableId: Int
    val additionalVariables: List<Pair<Int, Any>>

    constructor(variableId: Int) {
        this.layoutId = 0
        this.variableId = variableId
        this.additionalVariables = ArrayList()
    }

    constructor(@LayoutRes layoutId: Int, variableId: Int) {
        this.layoutId = layoutId
        this.variableId = variableId
        this.additionalVariables = ArrayList()
    }

    constructor(variableId: Int, additionalVariables: List<Pair<Int, Any>>) {
        this.layoutId = 0
        this.variableId = variableId
        this.additionalVariables = additionalVariables
    }

    constructor(@LayoutRes layoutId: Int, variableId: Int, additionalVariables: List<Pair<Int, Any>>) {
        this.layoutId = layoutId
        this.variableId = variableId
        this.additionalVariables = additionalVariables
    }

    abstract fun getItemType(item: Any): Int

    @LayoutRes
    abstract fun getItemLayout(itemType: Int): Int

}
