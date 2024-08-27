package com.soumyadip.otpforwardapp.domain.dto

class ValidationError : Exception {
    val isForwardPolicyInvalid: Boolean
    val isFilterListInvalid: Boolean
    val isContactListInvalid: Boolean

    constructor(
        isForwardPolicyInvalid: Boolean = false,
        isContactListInvalid: Boolean = false,
        isFilterListInvalid: Boolean = false
    )
            : super("Validation Error Occured") {
        this.isForwardPolicyInvalid = isForwardPolicyInvalid
        this.isContactListInvalid = isContactListInvalid
        this.isFilterListInvalid = isFilterListInvalid

    }
}