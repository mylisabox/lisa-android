package com.mylisabox.common.device

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mylisabox.common.ui.IpCameraView
import com.mylisabox.network.devices.models.*
import com.mylisabox.network.devices.models.Button
import com.mylisabox.network.devices.models.ImageButton
import com.mylisabox.network.devices.models.Space
import com.mylisabox.network.devices.models.ToggleButton
import com.mylisabox.network.devices.models.builder.Template
import com.mylisabox.network.devices.models.builder.TemplateBuilderVisitor
import com.mylisabox.network.utils.BaseUrlProvider
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.*

abstract class BaseTemplateBuilderVisitor(protected val context: Context,
                                          private val baseUrlProvider: BaseUrlProvider,
                                          layoutParams: ViewGroup.LayoutParams) : TemplateBuilderVisitor {
    protected val layoutParams: MutableList<ViewGroup.LayoutParams> = ArrayList()

    protected val lastLayoutParams: ViewGroup.LayoutParams
        get() = layoutParams[layoutParams.size - 1]

    init {
        this.layoutParams.add(layoutParams)
    }


    override fun build(component: Space): Template {
        val space = View(context)
        space.tag = component.name
        space.layoutParams = lastLayoutParams
        return Template(space)
    }


    override fun build(component: ToggleButton): Template {
        val toggle = Switch(context)
        toggle.tag = component.name
        toggle.layoutParams = lastLayoutParams
        toggle.setOnCheckedChangeListener { _, isChecked ->
            val key = component.infos!!["value"] as String
            component.onChange.onNext(WidgetEvent(component.device.get()!!, key, isChecked))
        }
        return Template(toggle)
    }


    override fun build(component: VBox): Template {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = lastLayoutParams
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.tag = component.name
        val onChildrenChange = ArrayList<Observable<WidgetEvent<Any>>>()

        for (child in component.children) {
            val childParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, child.flex)
            layoutParams.add(childParams)
            linearLayout.addView(child.buildView(this).view)
            layoutParams.remove(childParams)
            onChildrenChange.add(child.onChange)
        }
        component.childrenSubscription = Observable.merge<WidgetEvent<Any>>(onChildrenChange)
                .subscribeBy(
                        onNext = { component.onChange.onNext(it) },
                        onError = { Timber.e(it) })
        return Template(linearLayout)
    }


    override fun build(component: HBox): Template {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = lastLayoutParams
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.tag = component.name
        val onChildrenChange = ArrayList<Observable<WidgetEvent<Any>>>()

        for (child in component.children) {
            val childParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, child.flex)
            layoutParams.add(childParams)
            linearLayout.addView(child.buildView(this).view)
            layoutParams.remove(childParams)
            onChildrenChange.add(child.onChange)
        }
        component.childrenSubscription = Observable.merge(onChildrenChange).subscribeBy(
                onNext = { component.onChange.onNext(it) },
                onError = { Timber.e(it) }
        )
        return Template(linearLayout)
    }


    override fun build(component: Slider): Template {
        val seekBar = SeekBar(context)
        seekBar.layoutParams = lastLayoutParams
        seekBar.tag = component.name
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

            }


            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }


            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val key = component.infos!![Slider.KEY] as String
                val newValue = java.lang.Double.valueOf(seekBar.progress.toDouble())
                component.device.get()!!.data[key] = newValue
                component.onChange.onNext(WidgetEvent(component.device.get()!!, component.name!!, newValue))
            }
        })
        return Template(seekBar)
    }


    override fun build(component: Image): Template {
        val imageView = ImageView(context)
        imageView.tag = component.name
        imageView.layoutParams = lastLayoutParams
        return Template(imageView)
    }


    override fun build(component: ImageButton): Template {
        val imageButton = AppCompatImageView(context)
        imageButton.tag = component.name
        imageButton.layoutParams = lastLayoutParams
        return Template(imageButton)
    }

    override fun build(component: Card): Template {
        val frameLayout = FrameLayout(context)
        frameLayout.layoutParams = lastLayoutParams

        for (child in component.children) {
            val childParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            layoutParams.add(childParams)
            frameLayout.addView(child.buildView(this).view)
            layoutParams.remove(childParams)
        }
        return Template(frameLayout)
    }


    override fun build(component: Camera): Template {
        val view = IpCameraView(context, baseUrlProvider)
        view.tag = component.name
        view.layoutParams = lastLayoutParams
        return Template(view)
    }


    override fun build(component: Button): Template {
        val button = android.widget.Button(context)
        button.layoutParams = lastLayoutParams
        button.tag = component.name
        button.setOnClickListener { _ ->
            val key = component.infos!!["text"] as String
            if (component.value == null) {
                component.value = ""
            }
            component.onChange.onNext(WidgetEvent(component.device.get()!!, key, component.value!!))
        }
        return Template(button)
    }

}
