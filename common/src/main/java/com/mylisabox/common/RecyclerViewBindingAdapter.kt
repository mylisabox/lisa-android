package com.mylisabox.common

import android.databinding.BindingAdapter
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import com.mylisabox.common.ui.list.BindableListAdapter
import com.mylisabox.common.ui.list.ItemBinder
import com.mylisabox.common.ui.list.MultipleTypeItemBinder

object RecyclerViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("items", "itemLayout")
    fun <T> setRecyclerItems(recyclerView: RecyclerView, items: List<T>, @LayoutRes itemLayout: Int) {
        setRecyclerItems(recyclerView, items, ItemBinder(itemLayout, BR.data), null)
    }

    @JvmStatic
    @BindingAdapter("items", "itemLayout", "onItemClick")
    fun <T> setRecyclerItems(recyclerView: RecyclerView, items: List<T>, @LayoutRes itemLayout: Int,
                             onItemClick: BindableListAdapter.OnClickListener<T>?) {
        setRecyclerItems(recyclerView, items, ItemBinder(itemLayout, BR.data), onItemClick)
    }

    private fun <T> setRecyclerItems(recyclerView: RecyclerView, items: List<T>, itemBinder: MultipleTypeItemBinder, listener: BindableListAdapter.OnClickListener<T>?) {
        var adapter: BindableListAdapter<T>? = recyclerView.adapter as? BindableListAdapter<T>

        if (adapter == null) {
            adapter = BindableListAdapter(items, itemBinder)
            adapter.setOnClickListener(listener)
            recyclerView.adapter = adapter
        } else {
            adapter.setOnClickListener(listener)
            adapter.setItemBinder(itemBinder)
            adapter.setItems(items)
        }
    }
}
