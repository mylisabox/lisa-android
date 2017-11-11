package com.mylisabox.lisa.profile

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseFragment
import com.mylisabox.lisa.databinding.ProfileFragmentBinding
import com.mylisabox.network.dagger.annotations.Qualifiers.ForFragment
import javax.inject.Inject

class ProfileFragment : BaseFragment<ProfileViewModel>() {
    companion object {
        fun newInstance(): ProfileFragment {
            val fragment = ProfileFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    @ForFragment lateinit var vModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun inject() {
        getFragmentComponent(this).inject(this)
        val binding: ProfileFragmentBinding? = DataBindingUtil.bind(view!!)
        binding?.viewModel = vModel
        binding?.executePendingBindings()
    }

    override fun getViewModel(): ProfileViewModel? {
        return vModel
    }
}