package com.mylisabox.lisa.login

import android.os.Bundle
import com.mylisabox.common.BaseViewModel
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseActivity

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Enables Always-on
        setAmbientEnabled()
    }

    override fun getViewModel(): BaseViewModel? {
        return null
    }
}
