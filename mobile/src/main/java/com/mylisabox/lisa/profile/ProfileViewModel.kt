package com.mylisabox.lisa.profile

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.util.PatternsCompat
import android.util.Patterns
import com.mylisabox.common.BaseViewModel
import com.mylisabox.common.user.UserRepository
import com.mylisabox.lisa.R
import com.mylisabox.network.user.models.User
import com.mylisabox.network.utils.BaseUrlProvider
import com.mylisabox.network.utils.RxErrorForwarder
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val profileNavigator: ProfileNavigator,
                                           private val baseUrlProvider: BaseUrlProvider,
                                           private val rxErrorForwarder: RxErrorForwarder,
                                           private val userRepository: UserRepository) : BaseViewModel() {
    val user: ObservableField<User> = ObservableField()
    val avatarUrl: ObservableField<String> = ObservableField()
    val avatar: ObservableField<File> = ObservableField()
    val password: ObservableField<String> = ObservableField("")
    val verifyPassword: ObservableField<String> = ObservableField("")
    val phone: ObservableField<String> = ObservableField("")
    val email: ObservableField<String> = ObservableField("")
    val lastName: ObservableField<String> = ObservableField("")
    val firstName: ObservableField<String> = ObservableField("")
    val emailError: ObservableInt = ObservableInt()
    val passwordError: ObservableInt = ObservableInt()
    val phoneError: ObservableInt = ObservableInt()

    override fun bind() {
        super.bind()
        profileNavigator.showLoading()
        subscriptions?.add(userRepository.getProfile()
                .compose(rxErrorForwarder::catchExceptions)
                .subscribeBy(onError = {
                    profileNavigator.hideLoading()
                    Timber.e(it)
                }, onSuccess = {
                    profileNavigator.hideLoading()
                    setupUserData(it)
                })
        )
    }

    private fun setupUserData(userData: User) {
        user.set(userData)
        var userAvatar = user.get()!!.avatar
        if (userAvatar != null && userAvatar.startsWith("/")) {
            userAvatar = baseUrlProvider.getBaseUrl() + userAvatar
        }
        avatarUrl.set(userAvatar)
        email.set(userData.email)
        lastName.set(userData.lastName ?: "")
        firstName.set(userData.firstName ?: "")
        phone.set(userData.mobile ?: "")
    }

    fun saveProfile() {
        if (isFormValid()) {
            subscriptions?.add(userRepository.updateProfile(User(user.get()!!.id, email.get()!!,
                    user.get()!!.avatar, firstName.get(), user.get()!!.lang, lastName.get(), phone.get(), password.get()), avatar.get())
                    .compose(rxErrorForwarder::catchExceptions)
                    .subscribeBy(onError = Timber::e, onSuccess = {
                        profileNavigator.goBack()
                    })
            )

        }
    }

    private fun isFormValid(): Boolean {
        var isValid = true

        if (email.get()!!.isEmpty() || email.get()!!.isBlank()) {
            emailError.set(R.string.error_field_required)
            isValid = false
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email.get()).matches()) {
            emailError.set(R.string.error_field_email)
            isValid = false
        }
        if (!phone.get()!!.isEmpty() && !Patterns.PHONE.matcher(phone.get()).matches()) {
            phoneError.set(R.string.error_field_phone)
            isValid = false
        }
        if (!password.get()!!.isEmpty() && password.get()!!.length < 8) {
            passwordError.set(R.string.error_password_lenght)
            isValid = false
        }
        if (password.get() != verifyPassword.get()) {
            passwordError.set(R.string.error_password_dont_match)
            isValid = false
        }

        return isValid
    }

    fun choosePicture() {
        profileNavigator.goToPictureSelector()
    }
}