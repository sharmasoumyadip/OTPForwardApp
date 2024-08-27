package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository

class UpdatePolicyUseCase(private val forwardPolicyRepository: OTPForwardRepository) {
    suspend operator fun invoke(forwardPolicyDetails: ForwardPolicyDetails) {
        forwardPolicyRepository.updatePolicy(forwardPolicyDetails)
    }
}