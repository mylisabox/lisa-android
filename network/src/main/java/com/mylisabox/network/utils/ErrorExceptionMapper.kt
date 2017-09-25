package com.mylisabox.network.utils

import com.mylisabox.network.R
import com.mylisabox.network.exceptions.NoEndPointException
import com.mylisabox.network.utils.RxErrorForwarder.ExceptionMapper
import javax.inject.Inject

class ErrorExceptionMapper @Inject constructor() : ExceptionMapper {
    override fun getMessage(throwable: Throwable): Int {
        return when (throwable) {
            is NoEndPointException ->
                R.string.error_no_endpoint
            else ->
                R.string.error_generic_ws
        }
    }
}