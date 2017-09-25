package com.mylisabox.lisa.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.lisa.databinding.ActivityHomeBinding
import javax.inject.Inject

class HomeActivity : BaseActivity() {

    @Inject lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)

        activityComponent.inject(this)

        binding.viewModel = getViewModel()
        // Enables Always-on
        setAmbientEnabled()
    }

    override fun getViewModel(): HomeViewModel? {
        return homeViewModel
    }
}
