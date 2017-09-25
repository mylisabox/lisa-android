package com.mylisabox.network.devices.models

import io.reactivex.disposables.Disposable
import java.util.*

abstract class BaseGroup : BaseElement() {
    lateinit var childrenSubscription: Disposable
    var children: List<BaseElement> = ArrayList()

    override fun associateDevice(device: Device) {
        super.associateDevice(device)
        for (child in children) {
            child.associateDevice(device)
        }
    }
}
