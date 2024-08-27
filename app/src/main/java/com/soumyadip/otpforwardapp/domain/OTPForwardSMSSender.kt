package com.soumyadip.otpforwardapp.domain

import com.soumyadip.otpforwardapp.domain.dto.SMSData

interface OTPForwardSMSSender {
    suspend fun sendSMS(toBeSentSMSList: List<SMSData>)
}