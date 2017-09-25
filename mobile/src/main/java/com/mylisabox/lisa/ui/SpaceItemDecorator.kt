package com.mylisabox.lisa.ui

import android.support.v7.widget.RecyclerView.ItemDecoration

class SpaceItemDecorator(val left: Int,
                         val top: Int,
                         val right: Int,
                         val bottom: Int) : ItemDecoration() {


    constructor(rect: android.graphics.Rect) : this(rect.left, rect.top, rect.right, rect.bottom)

    override fun getItemOffsets(outRect: android.graphics.Rect?, view: android.view.View?, parent: android.support.v7.widget.RecyclerView?, state: android.support.v7.widget.RecyclerView.State?) {
        outRect?.left = this.left
        outRect?.top = this.top
        outRect?.right = this.right
        outRect?.bottom = this.bottom
    }

}