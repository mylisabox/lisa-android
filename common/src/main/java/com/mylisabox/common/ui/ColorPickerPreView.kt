package com.mylisabox.common.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.Nullable
import android.util.AttributeSet
import android.view.View
import com.mylisabox.common.utils.DimensionUtils


class ColorPickerPreView : View {
    var color: Int = 0
        set(color) {
            field = color
            invalidate()
        }
    private val bgPaint = Paint()
    private val borderPaint = Paint()
    private val borderPixelSize: Float

    constructor(context: Context) : super(context)
    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        this.borderPaint.color = Color.BLACK
        this.borderPaint.style = Paint.Style.STROKE
        this.borderPaint.strokeWidth = 1F

        this.bgPaint.color = Color.GREEN
        this.borderPaint.style = Paint.Style.FILL
        this.borderPixelSize = DimensionUtils().convertDpToPixel(1F)
    }


    override fun onDraw(canvas: Canvas) {
        bgPaint.color = this.color

        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), borderPaint)
        canvas.drawRect(borderPixelSize, borderPixelSize, width - borderPixelSize, height - borderPixelSize, bgPaint)
    }
}
