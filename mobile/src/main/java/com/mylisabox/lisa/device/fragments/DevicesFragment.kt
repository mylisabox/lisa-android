package com.mylisabox.lisa.device.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mylisabox.common.device.ui.DevicesBindableAdapter
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseFragment
import com.mylisabox.lisa.databinding.DevicesFragmentBinding
import com.mylisabox.lisa.device.viewmodels.DeviceViewModel

/**
 * Created by jaumard on 07.12.16.
 * L.I.S.A. project license GPL-3
 */

abstract class DevicesFragment : BaseFragment<DeviceViewModel>() {
    private lateinit var touchHelper: ItemTouchHelper

    lateinit var vModel: DeviceViewModel

    override fun getViewModel(): DeviceViewModel {
        return vModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.devices_fragment, container, false)
    }

    @CallSuper
    override fun inject() {
        getFragmentComponent(this).inject(this)
        val binding = DataBindingUtil.bind<DevicesFragmentBinding>(this.view)
        binding.viewModel = vModel

        val callback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                if (viewHolder.itemViewType != target.itemViewType) {
                    return false
                }
                (binding.recyclerView.adapter as DevicesBindableAdapter).onItemMove(viewHolder.layoutPosition, target.layoutPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return false
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?) {
                super.clearView(recyclerView, viewHolder)
                val adapter = recyclerView.adapter as DevicesBindableAdapter
                vModel.saveWidgetOrder(adapter.getItems())
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ACTION_STATE_DRAG) {
                    vModel.isDragging.set(true)
                } else {
                    vModel.isDragging.set(false)
                }
            }
        }

        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recyclerView)
    }

/*
    fun onWidgetsOrderChanged(roomId: String, ids: List<String>) {

    }

    fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper!!.startDrag(viewHolder)
    }*/
}
