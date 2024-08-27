package com.soumyadip.otpforwardapp.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class NewSMSReceiver(private val processSMSCallBack: (String, String) -> Unit) :
    BroadcastReceiver() {
    private val currIntent = Telephony.Sms.Intents.SMS_RECEIVED_ACTION

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (intent.action == null || intent.action != currIntent) return
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (smsMessage in messages) {
            val sender = smsMessage.displayOriginatingAddress
            val messageBody = smsMessage.messageBody
            processSMSCallBack(sender, messageBody)


        }
    }
}