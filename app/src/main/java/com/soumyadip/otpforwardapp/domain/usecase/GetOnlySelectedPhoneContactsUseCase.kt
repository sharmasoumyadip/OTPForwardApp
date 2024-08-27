package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetOnlySelectedPhoneContactsUseCase() {
    suspend operator fun invoke(allPhoneContact: List<PhoneContact>): List<PhoneContact> {
        return withContext(Dispatchers.Default) {
            allPhoneContact.filter { it.isSelected }
        }
    }
}