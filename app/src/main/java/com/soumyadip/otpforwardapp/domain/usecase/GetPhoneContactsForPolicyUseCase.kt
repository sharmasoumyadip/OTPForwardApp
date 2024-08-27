package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository

class GetPhoneContactsForPolicyUseCase(private val otpForwardRepository: OTPForwardRepository) {
    suspend operator fun invoke(policyId: Long): List<String> {
        if (policyId == 0L) return emptyList()
        return otpForwardRepository.getSavedContactsForPolicy(policyId)
    }
}