package com.soumyadip.otpforwardapp.domain.dto


data class ForwardPolicyDetails(
    val policyId: Long = 0L,
    var policyName: String,
    var uniqueMessageIdentifier: String? = null,
    var isActive: Boolean = false
)