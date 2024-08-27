package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.dto.PolicyFilterAndContacts
import com.soumyadip.otpforwardapp.domain.dto.ValidationError


class ValidatePolicyFilterAndContactsUseCase(
    private val validateForwardPolicyUseCase: ValidateForwardPolicyUseCase,
    private val validateContactListUseCase: ValidateContactListUseCase,
    private val validateFiltersListUseCase: ValidateFiltersListUseCase
) {
    suspend operator fun invoke(
        forwardPolicyDetails: ForwardPolicyDetails,
        policyFilterAndContacts: PolicyFilterAndContacts
    ): Boolean {
        val isForwardPolicyValid = validateForwardPolicyUseCase(forwardPolicyDetails)
        val isContactListValid =
            validateContactListUseCase(policyFilterAndContacts.selectedContactList)
        val isFilterListValid = validateFiltersListUseCase(policyFilterAndContacts.savedFilters)
        val validation = isForwardPolicyValid && isContactListValid && isFilterListValid
        if (!validation) throw ValidationError(
            !isForwardPolicyValid,
            !isContactListValid,
            !isFilterListValid
        )
        return true

    }
}