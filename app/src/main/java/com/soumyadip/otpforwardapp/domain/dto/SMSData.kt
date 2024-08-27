package com.soumyadip.otpforwardapp.domain.dto


data class SMSData(
    val listOfRecipients: List<String>,
    val finalMessage: String
)
