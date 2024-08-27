package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.OTPForwardSMSSender
import com.soumyadip.otpforwardapp.domain.dto.SMSData

class ProcessSMSUseCase(
    private val getOnlyActivePolicyUseCase: GetOnlyActivePolicyUseCase,
    private val checkIfPolicyIsTriggered: CheckIfPolicyIsTriggered,
    private val getContactListForwardPolicyUseCase: GetPhoneContactsForPolicyUseCase,
    private val OTPForwardSmsSender: OTPForwardSMSSender
) {
    suspend operator fun invoke(senderId: String, meessageBody: String) {
        val activePolicyList = getOnlyActivePolicyUseCase()
        val toBeSentSMSList = mutableListOf<SMSData>()
        for (activePolicy in activePolicyList) {
            if (checkIfPolicyIsTriggered(activePolicy.policyId, senderId, meessageBody)) {
                var finalMessage = "Below is Auto-Forwarded Message"
                if (!activePolicy.uniqueMessageIdentifier.isNullOrBlank()) finalMessage += "\nUnique Identifier= ${activePolicy.uniqueMessageIdentifier}"
                finalMessage += "\nSender = ${senderId}"
                finalMessage += "\n\n${meessageBody}"
                val contactList =
                    getContactListForwardPolicyUseCase(policyId = activePolicy.policyId)
                toBeSentSMSList.add(
                    SMSData(
                        listOfRecipients = contactList,
                        finalMessage = finalMessage
                    )
                )
            }
        }
        OTPForwardSmsSender.sendSMS(toBeSentSMSList)
    }
}