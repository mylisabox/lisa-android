package com.mylisabox.lisa.common.adapters

import android.content.Context
import android.content.res.Resources
import android.databinding.BindingAdapter
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.mylisabox.lisa.R
import com.mylisabox.lisa.home.MainMenuViewModel
import com.mylisabox.lisa.utils.AnimationUtils
import com.mylisabox.network.room.models.Room

object MainMenuBindingAdapter {
    private val ANIMATION_DURATION = 500

    private fun getDividerHeight(): Float {
        return ((1 * Resources.getSystem().displayMetrics.density).toInt()).toFloat()
    }

    private fun getItemHeight(context: Context): Float {
        val value = TypedValue()
        val metrics = DisplayMetrics()
        context.theme.resolveAttribute(android.R.attr.listPreferredItemHeight, value, true)
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(metrics)
        return TypedValue.complexToDimension(value.data, metrics)
    }

    @JvmStatic
    @BindingAdapter("toggle", "targetHeight")
    fun setToggle(view: View, toggle: Boolean, targetHeight: Int) {
        if (toggle) {
            val itemHeight = getItemHeight(view.context) * targetHeight + getDividerHeight() * targetHeight
            AnimationUtils().expand(view, ANIMATION_DURATION, itemHeight.toInt())
        } else {
            AnimationUtils().collapse(view, ANIMATION_DURATION, 0)
        }
    }

    @JvmStatic
    @BindingAdapter("rooms", "onItemClick")
    fun setRooms(container: LinearLayout, rooms: List<Room>, onRoomClick: MainMenuViewModel.OnRoomListener) {
        val layoutInflater = LayoutInflater.from(container.context)
        val count = container.childCount
        for (i in 0 until count) {
            val view = container.getChildAt(i)
            if (view != null) {
                val roomId = view.tag as Number
                var found = false
                for ((id) in rooms) {
                    if (id == roomId) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    container.removeView(view)
                }
            }
        }
        for (i in rooms.indices) {
            val (id, name) = rooms[i]
            var textView: TextView? = container.findViewWithTag(id)
            if (textView == null) {
                textView = layoutInflater.inflate(R.layout.room_item, container, false) as TextView
                textView.text = name
                textView.tag = id
                textView.setOnClickListener { view -> onRoomClick.onRoomClicked(view.tag as Long, (view as TextView).text as String) }
                container.addView(textView)
            } else {
                textView.text = name
            }
        }
    }

    @JvmStatic
    @BindingAdapter("onRoomCreate")
    fun setOnRoomCreateListener(editText: EditText, onRoomCreate: MainMenuViewModel.OnRoomListener) {
        editText.setOnEditorActionListener { _, _, _ ->
            val roomName = editText.text.toString()
            if (!roomName.isEmpty()) {
                onRoomCreate.onRoomCreate(roomName)
                val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }

    }
}