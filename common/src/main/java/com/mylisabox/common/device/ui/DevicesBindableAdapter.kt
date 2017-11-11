package com.mylisabox.common.device.ui

import com.arasthel.spannedgridlayoutmanager.SpanLayoutParams
import com.arasthel.spannedgridlayoutmanager.SpanSize
import com.jaumard.recyclerviewbinding.BindableRecyclerAdapter
import com.jaumard.recyclerviewbinding.MultipleTypeItemBinder
import com.mylisabox.network.devices.models.Device
import java.util.*

class DevicesBindableAdapter(items: List<Device>,
                             itemBinder: MultipleTypeItemBinder) : BindableRecyclerAdapter<Device>(items, itemBinder) {
    companion object {
        const val WIDTH_UNIT_VALUE = 8
        private const val HEIGHT_UNIT_VALUE = 5
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val device = items[position]
        val width = device.template.widgetWidth * Companion.WIDTH_UNIT_VALUE
        val height = device.template.widgetHeight * Companion.HEIGHT_UNIT_VALUE
        holder.itemView.layoutParams = SpanLayoutParams(SpanSize(width, height))
    }

}