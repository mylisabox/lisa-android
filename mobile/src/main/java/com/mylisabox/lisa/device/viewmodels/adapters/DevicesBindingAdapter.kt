package com.mylisabox.lisa.device.viewmodels.adapters

import android.content.Context
import android.databinding.BindingAdapter
import android.support.annotation.LayoutRes
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import android.widget.LinearLayout
import com.jaumard.recyclerviewbinding.ItemBinder
import com.jaumard.recyclerviewbinding.RecyclerViewBindingAdapter
import com.mylisabox.common.device.TemplateViewPopulator
import com.mylisabox.common.device.WidgetHandler
import com.mylisabox.common.device.ui.DevicesBindableAdapter
import com.mylisabox.common.ui.ToggleImageButton
import com.mylisabox.lisa.BR
import com.mylisabox.lisa.common.TemplateMobileViewBuilder
import com.mylisabox.network.devices.models.Device

private const val WIDGET_WIDTH = 300f

private const val WIDGET_MARGIN = 15f

object DevicesBindingAdapter {
    @JvmStatic
    @BindingAdapter("device", "widgetHandler")
    fun setDeviceFavoriteToggle(imageToggleImageButton: ToggleImageButton, device: Device, widgetHandler: WidgetHandler) {
        imageToggleImageButton.setOnCheckedChangeListener(null)
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
        val template = device.template.buildView(templateBuilder)
        template.reset()
        val view = template.view

        container.addView(view)
        device.template.populateBy(templateViewPopulator)
        widgetHandler.listenForWidgetEvents(device.template.onChange)
    }

    @JvmStatic
    @BindingAdapter("devices", "itemLayout", "builder", "populator", "widgetHandler")
    fun setRecyclerDevices(recyclerView: RecyclerView, items: List<Device>, @LayoutRes itemLayout: Int,
                           templateMobileViewBuilder: TemplateMobileViewBuilder?, templateViewPopulator: TemplateViewPopulator, widgetHandler: WidgetHandler) {
        val additional = ArrayList<Pair<Int, Any>>()
        additional.add(Pair(BR.builder, templateMobileViewBuilder))
        additional.add(Pair(BR.populator, templateViewPopulator))
        additional.add(Pair(BR.widgetHandler, widgetHandler))
        val itemBinder = ItemBinder(BR.data, itemLayout, additional)
        RecyclerViewBindingAdapter.setRecyclerItems(recyclerView, items, itemBinder, null, DevicesBindableAdapter(items, itemBinder))
    }

    fun getSpanSizeFromScreenWidth(context: Context, recyclerView: RecyclerView): Int {
        val metrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)

        val availableWidth = recyclerView.width.toFloat()
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDGET_WIDTH, context.resources.displayMetrics)
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, WIDGET_MARGIN, context.resources.displayMetrics)
        return Math.max(1, Math.floor((availableWidth / (px + margin * 2)).toDouble()).toInt()) * DevicesBindableAdapter.WIDTH_UNIT_VALUE
    }

}