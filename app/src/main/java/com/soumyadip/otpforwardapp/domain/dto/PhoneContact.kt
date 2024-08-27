package com.soumyadip.otpforwardapp.domain.dto

data class PhoneContact(
    val id: Long,
    val displayName: String,
    val phoneNumber: String,
    var isSelected: Boolean = false
)
