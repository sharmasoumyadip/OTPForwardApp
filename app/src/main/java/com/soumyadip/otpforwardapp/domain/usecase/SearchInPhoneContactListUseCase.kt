package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchInPhoneContactListUseCase() {
    suspend operator fun invoke(
        query: String?,
        phoneContactList: List<PhoneContact>
    ): List<PhoneContact> {
        val trimmedQuery = query?.trim()?.uppercase() ?: ""
        return withContext(Dispatchers.Default) {
            if (trimmedQuery.isBlank()) return@withContext phoneContactList
            phoneContactList.filter {
                it.displayName.uppercase().contains(trimmedQuery)
            }
        }
    }
}