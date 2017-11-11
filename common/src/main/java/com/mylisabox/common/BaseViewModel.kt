package com.mylisabox.common

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    protected lateinit var subscriptions: CompositeDisposable

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
    @CallSuper
    open fun bind() {
        unbind()
        subscriptions = CompositeDisposable()
    }

    @CallSuper
    open fun unbind() {
        if (this::subscriptions.isInitialized) {
            subscriptions.clear()
        }
    }
}