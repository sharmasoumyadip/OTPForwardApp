package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository

class GetOnlyActivePolicyUseCase(private val forwardPolicyRepository: OTPForwardRepository) {
    suspend operator fun invoke(): List<ForwardPolicyDetails> {
        return forwardPolicyRepository.getOnlyActivePolicies()
    }
}