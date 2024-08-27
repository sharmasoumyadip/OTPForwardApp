package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository

class GetFiltersForPolicyUseCase(private val otpForwardRepository: OTPForwardRepository) {
    suspend operator fun invoke(policyId: Long): List<Filter> {
        if (policyId == 0L) return emptyList()
        return otpForwardRepository.getSavedFiltersForPolicy(policyId)
    }

}