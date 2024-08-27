package com.soumyadip.otpforwardapp.domain.dto

data class Contact(
    val id: Int,
    val userName: String,
    val firstName: String,
    val lastName: String?,
    val email: String
)