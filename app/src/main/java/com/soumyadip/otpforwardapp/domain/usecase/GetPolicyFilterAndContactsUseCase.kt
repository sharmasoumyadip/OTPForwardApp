package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import com.soumyadip.otpforwardapp.domain.dto.PolicyFilterAndContacts

class GetPolicyFilterAndContactsUseCase(
    private val getFiltersForPolicyUseCase: GetFiltersForPolicyUseCase,
    private val getAllContactsWithSelectedForPolicy: GetAllContactsWithSelectedForPolicy,
    private val getOnlySelectedPhoneContactsUseCase: GetOnlySelectedPhoneContactsUseCase
) {
    suspend operator fun invoke(
        policyId: Long,
        allPhoneContactsListArg: List<PhoneContact>?,
        filterListArg: List<Filter>?
    ): PolicyFilterAndContacts {
        val allPhoneContactsList =
            allPhoneContactsListArg ?: getAllContactsWithSelectedForPolicy(policyId)
        val filtersList = filterListArg ?: getFiltersForPolicyUseCase(policyId)
        val onlySelectedPhoneContact = getOnlySelectedPhoneContactsUseCase(allPhoneContactsList)
        return PolicyFilterAndContacts(
            policyId,
            allPhoneContactsList,
            onlySelectedPhoneContact,
            filtersList
        )
    }
}