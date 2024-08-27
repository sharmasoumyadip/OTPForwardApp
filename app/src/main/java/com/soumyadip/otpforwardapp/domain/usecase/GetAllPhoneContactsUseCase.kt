package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllPhoneContactsUseCase(private val otpForwardRepository: OTPForwardRepository) {
    suspend operator fun invoke(): List<PhoneContact> {
        return withContext(Dispatchers.Default) {
            val allContacts =
                otpForwardRepository.getAllPhoneContacts().filter { it.phoneNumber.isNotBlank() }
            val hashSet = mutableSetOf<String>()
            val contactsWithoutDuplicate = mutableListOf<PhoneContact>()
            for (contact in allContacts) {
                val key = contact.displayName + "$" + contact.phoneNumber
                if (hashSet.contains(key)) continue
                hashSet.add(key)
                contactsWithoutDuplicate.add(contact)
            }
            contactsWithoutDuplicate
        }
    }
}