package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ValidateContactListUseCase() {
    suspend operator fun invoke(phoneContactList: List<PhoneContact>): Boolean {
        return withContext(Dispatchers.Default) {
            if (phoneContactList.isEmpty()) return@withContext false
            for (phoneContact in phoneContactList) {
                if (phoneContact.phoneNumber.isBlank()) return@withContext false
            }
            true

        }
    }
}