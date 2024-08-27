package com.soumyadip.otpforwardapp.domain.dto

sealed class FlowResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : FlowResult<T>(data)
    class Error<T>(message: String?) : FlowResult<T>(message = message)
    class Loading<T> : FlowResult<T>()

}