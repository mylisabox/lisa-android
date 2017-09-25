package com.mylisabox.lisa.room

import android.os.Bundle
import com.mylisabox.common.BaseViewModel
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseActivity

class RoomActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        // Enables Always-on
        setAmbientEnabled()
    }

    override fun getViewModel(): BaseViewModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
