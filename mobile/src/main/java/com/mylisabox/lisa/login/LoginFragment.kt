package com.mylisabox.lisa.login

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mylisabox.lisa.R
import com.mylisabox.lisa.common.BaseFragment
import com.mylisabox.lisa.databinding.LoginFragmentBinding
import com.mylisabox.lisa.login.LoginActivity.Companion.KEY_MODE
import com.mylisabox.network.dagger.annotations.FragmentScope
import com.mylisabox.network.dagger.annotations.Qualifiers.ForFragment
import javax.inject.Inject

@FragmentScope
class LoginFragment : BaseFragment<LoginViewModel>() {
    companion object {
        fun newInstance(mode: String): LoginFragment {
            val loginFragment = LoginFragment()
            val bundle = Bundle()
            bundle.putString(LoginActivity.KEY_MODE, mode)
            loginFragment.arguments = bundle
            return loginFragment
        }
    }

    @Inject
    @ForFragment lateinit var vModel: LoginViewModel

    override fun getViewModel(): LoginViewModel {
        return vModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun inject() {
        getFragmentComponent(this).inject(this)
        val binding: LoginFragmentBinding? = DataBindingUtil.bind(view!!)
        vModel.isRegistrationMode.set(arguments?.get(KEY_MODE) == LoginActivity.MODE_REGISTER)
        binding?.viewModel = vModel
        binding?.executePendingBindings()
    }

}