package com.mylisabox.network.utils

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast
import com.mylisabox.network.R
import com.mylisabox.network.dagger.annotations.ActivityScope
import com.mylisabox.network.dagger.annotations.Qualifiers.ForActivity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@ActivityScope
class RxErrorForwarder @Inject constructor(@ForActivity private val context: Context,
                                           private val loginNavigation: LoginNavigation,
                                           private val exceptionMapper: ExceptionMapper) {
    companion object {
        val UNAUTHORIZED = "HTTP 401 Unauthorized"
    }

    fun toast(@StringRes id: Int) {
        Toast.makeText(context, id, Toast.LENGTH_LONG).show()
    }

    fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun <T> catchExceptions(observable: Observable<T>): Observable<T> {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).doOnError({ throwable ->
            manageException(throwable)
        })
    }

    fun <T> catchExceptions(single: Single<T>): Single<T> {
        return single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).doOnError({ throwable ->
            manageException(throwable)
        })
    }

    fun <T> catchExceptions(maybe: Maybe<T>): Maybe<T> {
        return maybe.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).doOnError({ throwable ->
            manageException(throwable)
        })
    }

    fun catchExceptions(completable: Completable): Completable {
        return completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).doOnError({ throwable ->
            manageException(throwable)
        })
    }

    private fun manageException(throwable: Throwable) {
        Timber.w(throwable)
        if (throwable.message == UNAUTHORIZED) {
            toast(R.string.error_unauthorized)
            loginNavigation.goToLogin()
        } else {
            toast(exceptionMapper.getMessage(throwable))
        }
    }

    interface LoginNavigation {
        fun goToLogin()
    }

    interface ExceptionMapper {
        @StringRes
        fun getMessage(throwable: Throwable): Int
    }
}