package com.mylisabox.lisa.ui

import android.content.Context
import android.support.v4.widget.DrawerLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TabletDrawerLayout : DrawerLayout {
    private var interceptDisallowed: Boolean = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val drawer = getChildAt(1)
        return if (interceptDisallowed && ev.rawX > drawer.width) {
            super.onInterceptTouchEvent(ev)
            false
        } else {
            super.onInterceptTouchEvent(ev)
        }
    }

    override fun setDrawerLockMode(lockMode: Int) {
        super.setDrawerLockMode(lockMode)
        // if the drawer is locked, then disallow interception
        interceptDisallowed = lockMode == DrawerLayout.LOCK_MODE_LOCKED_OPEN
    }

    override fun getDrawerLockMode(drawerView: View): Int {
        return if (interceptDisallowed) DrawerLayout.LOCK_MODE_LOCKED_OPEN else DrawerLayout.LOCK_MODE_UNLOCKED
    }
}