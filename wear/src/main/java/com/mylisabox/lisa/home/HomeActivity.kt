package com.mylisabox.lisa.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearSnapHelper
import android.support.wear.widget.WearableLinearLayoutManager
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseActivity
import com.mylisabox.lisa.databinding.ActivityHomeBinding
import javax.inject.Inject

class HomeActivity : BaseActivity() {

    @Inject lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        redirectIfTokenExpired = false
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
        binding.recyclerView.isEdgeItemsCenteringEnabled = true

        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.layoutManager = WearableLinearLayoutManager(this)

        activityComponent.inject(this)

        binding.viewModel = getViewModel()
        // Enables Always-on
        setAmbientEnabled()
    }

    override fun getViewModel(): HomeViewModel? {
        return homeViewModel
    }
}
