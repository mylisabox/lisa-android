package com.mylisabox.lisa.device.fragments

import com.mylisabox.lisa.R
import com.mylisabox.lisa.device.viewmodels.NewDeviceViewModel
import javax.inject.Inject

/**
 * Created by jaumard on 07.12.16.
 * L.I.S.A. project license GPL-3
 */

class NewDevicesFragment : DevicesFragment() {
    companion object {
        fun newInstance(): NewDevicesFragment {
            return NewDevicesFragment()
        }
    }

    @Inject
    lateinit var newDeviceModel: NewDeviceViewModel

    override fun onStart() {
        super.onStart()
        activity?.title = getString(R.string.menu_new_devices)
    }

    override fun inject() {
        getFragmentComponent(this).inject(this)
        vModel = newDeviceModel
        super.inject()
    }
}