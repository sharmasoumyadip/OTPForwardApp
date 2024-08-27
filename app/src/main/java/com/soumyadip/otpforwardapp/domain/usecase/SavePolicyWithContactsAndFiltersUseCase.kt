package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.dto.PolicyFilterAndContacts
import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository

class SavePolicyWithContactsAndFiltersUseCase(
    private val forwardPolicyRepository: OTPForwardRepository,
    private val validatePolicyFilterAndContactsUseCase: ValidatePolicyFilterAndContactsUseCase
) {
    suspend operator fun invoke(
        forwardPolicyDetails: ForwardPolicyDetails,
        policyFilterAndContacts: PolicyFilterAndContacts
    ) {
        if (validatePolicyFilterAndContactsUseCase(forwardPolicyDetails, policyFilterAndContacts)) {
            forwardPolicyRepository.saveForwardPolicy(
                forwardPolicyDetails,
                policyFilterAndContacts.selectedContactList,
                policyFilterAndContacts.savedFilters
            )
        }
    }
}