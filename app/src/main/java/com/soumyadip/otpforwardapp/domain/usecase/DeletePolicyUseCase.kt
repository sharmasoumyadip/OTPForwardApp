package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository

class DeletePolicyUseCase(private val forwardPolicyRepository: OTPForwardRepository) {
    suspend operator fun invoke(policyDetails: ForwardPolicyDetails) {
        forwardPolicyRepository.deletePolicy(policyDetails)
    }
}