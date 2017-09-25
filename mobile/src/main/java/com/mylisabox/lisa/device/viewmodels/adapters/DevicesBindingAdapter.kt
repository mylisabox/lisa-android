package com.mylisabox.lisa.device.viewmodels.adapters

import android.content.Context
import android.databinding.BindingAdapter
import android.support.annotation.LayoutRes
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager
import com.mylisabox.common.device.TemplateViewPopulator
import com.mylisabox.common.device.WidgetHandler
import com.mylisabox.common.device.ui.DevicesBindableAdapter
import com.mylisabox.common.ui.ToggleImageButton
import com.mylisabox.common.ui.list.BindableListAdapter
import com.mylisabox.common.ui.list.ItemBinder
import com.mylisabox.common.ui.list.MultipleTypeItemBinder
import com.mylisabox.lisa.BR
import com.mylisabox.lisa.common.TemplateMobileViewBuilder
import com.mylisabox.lisa.ui.SpaceItemDecorator
import com.mylisabox.network.devices.models.Device

object DevicesBindingAdapter {
    @JvmStatic
    @BindingAdapter("device", "widgetHandler")
    fun setDeviceFavoriteToggle(imageToggleImageButton: ToggleImageButton, device: Device, widgetHandler: WidgetHandler) {
        imageToggleImageButton.isChecked = device.favorite
        imageToggleImageButton.setOnCheckedChangeListener(object : ToggleImageButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: ToggleImageButton, isChecked: Boolean) {
                widgetHandler.toggleFavorite(device)
            }
        })
    }

    @JvmStatic
    @BindingAdapter("device", "builder", "populator", "widgetHandler")
    fun setDeviceView(container: LinearLayout, device: Device, templateBuilder: TemplateMobileViewBuilder, templateViewPopulator: TemplateViewPopulator,
                      widgetHandler: WidgetHandler) {
        container.removeAllViews()

        device.template.associateDevice(device)
        val view = device.template.buildView(templateBuilder).view

        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view)
        }
        container.addView(view)
        device.template.populateBy(templateViewPopulator)

        widgetHandler.listenForWidgetEvents(device.template.onChange)
    }

    @JvmStatic
    @BindingAdapter("devices", "itemLayout", "builder", "populator", "widgetHandler")
    fun setRecyclerDevices(recyclerView: RecyclerView, items: List<Device>, @LayoutRes itemLayout: Int,
                           templateMobileViewBuilder: TemplateMobileViewBuilder, templateViewPopulator: TemplateViewPopulator, widgetHandler: WidgetHandler) {
        val additional = ArrayList<Pair<Int, Any>>()
        additional.add(Pair(BR.builder, templateMobileViewBuilder))
        additional.add(Pair(BR.populator, templateViewPopulator))
        additional.add(Pair(BR.widgetHandler, widgetHandler))
        val itemBinder = ItemBinder(itemLayout, BR.data, additional)
        DevicesBindingAdapter.setRecyclerDevices(recyclerView, items, itemBinder, null)
    }

    private fun getSpanSizeFromScreenWidth(context: Context, recyclerView: RecyclerView): Int {
        val metrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)

        val availableWidth = (recyclerView.parent as View).width.toFloat() //FIXME don't know why just recyclerView.width is always 0
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, context.resources.displayMetrics)
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 15f, context.resources.displayMetrics)
        return Math.max(1, Math.floor((availableWidth / (px + margin)).toDouble()).toInt()) * DevicesBindableAdapter.WIDTH_UNIT_VALUE
    }

    private fun setRecyclerDevices(recyclerView: RecyclerView, items: List<Device>, itemBinder: MultipleTypeItemBinder,
                                   listener: BindableListAdapter.OnClickListener<Device>?) {

        var adapter = recyclerView.adapter as? DevicesBindableAdapter
        if (adapter == null) {
            //Delay creation of layout manager to have recycler view rendered before
            recyclerView.post({
                val spannedGridLayoutManager = SpannedGridLayoutManager(orientation = SpannedGridLayoutManager.Orientation.VERTICAL,
                        spans = getSpanSizeFromScreenWidth(recyclerView.context, recyclerView))
                recyclerView.layoutManager = spannedGridLayoutManager
            })

            recyclerView.addItemDecoration(SpaceItemDecorator(left = 15, top = 15, right = 15, bottom = 15))

            adapter = DevicesBindableAdapter(items, itemBinder)
            adapter.setOnClickListener(listener)
            recyclerView.adapter = adapter
        } else {
            adapter.setOnClickListener(listener)
            adapter.setItemBinder(itemBinder)
            adapter.setItems(items)
        }
    }
}