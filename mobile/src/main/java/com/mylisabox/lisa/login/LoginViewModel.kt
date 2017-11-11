package com.mylisabox.lisa.login

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.util.PatternsCompat.EMAIL_ADDRESS
import com.mylisabox.common.BaseViewModel
import com.mylisabox.common.CommonApplication
import com.mylisabox.common.user.UserRepository
import com.mylisabox.lisa.R
import com.mylisabox.lisa.network.users.models.Credential
import com.mylisabox.lisa.utils.OnImeClicked
import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.utils.RxErrorForwarder
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userRepository: UserRepository,
                                         private val loginNavigator: LoginNavigator,
                                         private val preferences: Preferences,
                                         private val rxErrorForwarder: RxErrorForwarder) : BaseViewModel(), OnImeClicked {
    val email = ObservableField("")
    val password = ObservableField("")
    val emailError = ObservableInt()
    val passwordError = ObservableInt()
    val isRegistrationMode = ObservableBoolean()

    init {
        email.set(preferences.get(CommonApplication.KEY_EMAIL, ""))
    }

    override fun onImeClicked() {
        login()
    }

    fun goToSettings() {
        loginNavigator.goToSettings()
    }

    private fun isFormValid(): Boolean {
        var isValid = true

        if (email.get()!!.isEmpty() || email.get()!!.isBlank()) {
            emailError.set(R.string.error_field_required)
            isValid = false
        } else if (!EMAIL_ADDRESS.matcher(email.get()).matches()) {
            emailError.set(R.string.error_field_email)
            isValid = false
        }
        if (password.get()!!.isEmpty() || password.get()!!.isBlank()) {
            passwordError.set(R.string.error_field_required)
            isValid = false
        }
        return isValid
    }

    fun login() {
        val credential = Credential(email.get()!!, password.get()!!)
        if (isFormValid()) {
            loginNavigator.showLoading()
            val action = if (isRegistrationMode.get()) userRepository.create(credential) else userRepository.retrieve(credential)
            subscriptions?.add(action
                    .compose(rxErrorForwarder::catchExceptions)
                    .subscribeBy(onSuccess = {
                        loginNavigator.hideLoading()
                        loginNavigator.goToHome()
                        preferences.setAndApply(CommonApplication.KEY_EMAIL, email.get()!!)
                    }, onError = {
                        Timber.e(it)
                        loginNavigator.hideLoading()
                    })
            )
        }
    }
}