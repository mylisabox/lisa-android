package com.mylisabox.common.device.ui

import com.arasthel.spannedgridlayoutmanager.SpanLayoutParams
import com.arasthel.spannedgridlayoutmanager.SpanSize
import com.mylisabox.common.ui.list.BindableListAdapter
import com.mylisabox.common.ui.list.MultipleTypeItemBinder
import com.mylisabox.network.devices.models.Device
import java.util.*

class DevicesBindableAdapter(items: List<Device>,
                             itemBinder: MultipleTypeItemBinder) : BindableListAdapter<Device>(items, itemBinder) {
    companion object {
        val WIDTH_UNIT_VALUE = 8
        private val HEIGHT_UNIT_VALUE = 5
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val device = items[position]
        val width = device.template.widgetWidth * Companion.WIDTH_UNIT_VALUE
        val height = device.template.widgetHeight * Companion.HEIGHT_UNIT_VALUE
        holder.itemView.layoutParams = SpanLayoutParams(SpanSize(width, height))
    }

    override fun onViewRecycled(holder: BindingViewHolder) {
        super.onViewRecycled(holder)
        //holder.unbind()
    }

    override fun onViewDetachedFromWindow(holder: BindingViewHolder) {
        super.onViewDetachedFromWindow(holder)
        //holder.unbind()
    }

}