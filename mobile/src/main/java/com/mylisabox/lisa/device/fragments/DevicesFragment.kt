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
import android.view.ViewTreeObserver
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager
import com.mylisabox.common.device.ui.DevicesBindableAdapter
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseFragment
import com.mylisabox.lisa.common.TemplateMobileViewBuilder
import com.mylisabox.lisa.databinding.DevicesFragmentBinding
import com.mylisabox.lisa.device.viewmodels.DeviceViewModel
import com.mylisabox.lisa.device.viewmodels.adapters.DevicesBindingAdapter
import com.mylisabox.lisa.ui.SpaceItemDecorator
import javax.inject.Inject


/**
 * Created by jaumard on 07.12.16.
 * L.I.S.A. project license GPL-3
 */

abstract class DevicesFragment : BaseFragment<DeviceViewModel>() {
    private lateinit var touchHelper: ItemTouchHelper

    lateinit var vModel: DeviceViewModel
    @Inject
    lateinit var templateMobileViewBuilder: TemplateMobileViewBuilder
    private lateinit var binding: DevicesFragmentBinding

    override fun getViewModel(): DeviceViewModel {
        return vModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.devices_fragment, container, false)
    }

    private fun initBindings() {

        val callback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return if (vModel.isDragEnabled) ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0) else 0
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
                vModel.saveWidgetOrder(adapter.items)
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

        //Delay creation of layout manager to have recycler view rendered before the size calculation
        val spannedGridLayoutManager = SpannedGridLayoutManager(orientation = SpannedGridLayoutManager.Orientation.VERTICAL,
                spans = DevicesBindingAdapter.getSpanSizeFromScreenWidth(binding.recyclerView.context, binding.recyclerView))
        binding.recyclerView.addItemDecoration(SpaceItemDecorator(left = 15, top = 15, right = 15, bottom = 15))
        binding.recyclerView.layoutManager = spannedGridLayoutManager
    }

    override fun bind() {}

    @CallSuper
    override fun inject() {
        getFragmentComponent(this).inject(this)
        binding = DataBindingUtil.bind(this.view!!)!!
        vModel.templateMobileViewBuilder = templateMobileViewBuilder
        binding.viewModel = vModel

        view!!.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        initBindings()
                        vModel.bind()
                    }
                })
    }
}
