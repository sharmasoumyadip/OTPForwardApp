package com.soumyadip.otpforwardapp.infrastructure

import android.telephony.SmsManager
import com.soumyadip.otpforwardapp.domain.OTPForwardSMSSender
import com.soumyadip.otpforwardapp.domain.dto.SMSData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OTPForwardSMSSenderImpl(private val smsManager: SmsManager) : OTPForwardSMSSender {
    override suspend fun sendSMS(toBeSentSMSList: List<SMSData>) {
        withContext(Dispatchers.IO) {
            for (smsToSent in toBeSentSMSList) {
                val messageBody = smsToSent.finalMessage
                for (recipient in smsToSent.listOfRecipients) {
                    smsManager.sendTextMessage(recipient, null, messageBody, null, null)

                }
            }
        }
    }
}