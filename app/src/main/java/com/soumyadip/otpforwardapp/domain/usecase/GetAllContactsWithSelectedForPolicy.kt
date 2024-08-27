package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.PhoneContact


class GetAllContactsWithSelectedForPolicy(
    private val getAllPhoneContactsUseCase: GetAllPhoneContactsUseCase,
    private val getPhoneContactsForPolicyUseCase: GetPhoneContactsForPolicyUseCase
) {
    suspend operator fun invoke(policyId: Long): List<PhoneContact> {
        val selectedContactsForPolicy = getPhoneContactsForPolicyUseCase(policyId)
        val allPhoneContacts = getAllPhoneContactsUseCase().toMutableList()
        val tempPhoneContacts = mutableListOf<PhoneContact>()
        for (selectedContact in selectedContactsForPolicy) {
            var saved = false
            for (phoneContact in allPhoneContacts) {

                if (selectedContact.equals(phoneContact.phoneNumber)) {
                    phoneContact.isSelected = true
                    saved = true
                    break
                }
            }
            if (!saved) tempPhoneContacts.add(
                PhoneContact(
                    System.currentTimeMillis() * -1, "", selectedContact, true
                )
            )
        }
        return allPhoneContacts + tempPhoneContacts
    }
}