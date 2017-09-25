package com.mylisabox.lisa.common

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import com.mylisabox.common.BaseViewModel
import com.mylisabox.lisa.dagger.components.FragmentComponent
import com.mylisabox.lisa.dagger.modules.FragmentModule
import com.mylisabox.network.dagger.annotations.FragmentScope

@FragmentScope
abstract class BaseFragment<out T : BaseViewModel> : Fragment() {

    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inject()
    }

    protected abstract fun inject()

    protected fun getFragmentComponent(fragment: Fragment): FragmentComponent {
        return (activity as BaseActivity).activityComponent.plusFragmentComponent(FragmentModule(fragment))
    }

    protected abstract fun getViewModel(): T?

    fun setTitle(title: String) {
        activity?.title = title
    }

    fun setTitle(title: Int) {
        activity?.setTitle(title)
    }

    override fun onPause() {
        super.onPause()
        getViewModel()?.unbind()
    }

    override fun onResume() {
        super.onResume()
        getViewModel()?.bind()
    }
}