package com.mylisabox.common.ui.list

import android.support.annotation.IntegerRes
import android.support.annotation.LayoutRes
import android.support.v4.util.Pair

class ItemBinder : MultipleTypeItemBinder {

    constructor(variableId: Int) : super(variableId) {}

    constructor(@LayoutRes layoutId: Int, variableId: Int) : super(layoutId, variableId) {}

    constructor(@IntegerRes variableId: Int, additionalVariables: List<Pair<Int, Any>>) : super(variableId, additionalVariables) {}

    constructor(@LayoutRes layoutId: Int, @IntegerRes variableId: Int, additionalVariables: List<Pair<Int, Any>>) : super(layoutId, variableId, additionalVariables) {}

    override fun getItemType(item: Any): Int {
        return TYPE_NONE
    }

    @LayoutRes
    override fun getItemLayout(itemType: Int): Int {
        return layoutId
    }
}
