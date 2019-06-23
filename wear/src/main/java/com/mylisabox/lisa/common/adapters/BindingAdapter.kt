package com.mylisabox.lisa.common.adapters

import android.databinding.BindingAdapter
import android.support.annotation.LayoutRes
import android.support.wear.widget.WearableRecyclerView
import com.jaumard.recyclerviewbinding.BindableRecyclerAdapter
import com.jaumard.recyclerviewbinding.ItemBinder
import com.jaumard.recyclerviewbinding.RecyclerViewBindingAdapter
import com.mylisabox.lisa.BR
import com.mylisabox.lisa.home.MenuItem

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("items", "itemLayout", "onItemClick")
    fun setRecyclerDevices(recyclerView: WearableRecyclerView, items: List<MenuItem>,
                           @LayoutRes itemLayout: Int,
                           onClickListener: BindableRecyclerAdapter.OnClickListener<MenuItem>) {
        val itemBinder = ItemBinder(BR.data, itemLayout)
        RecyclerViewBindingAdapter.setRecyclerItems(recyclerView, items, itemBinder, onClickListener)
    }
}