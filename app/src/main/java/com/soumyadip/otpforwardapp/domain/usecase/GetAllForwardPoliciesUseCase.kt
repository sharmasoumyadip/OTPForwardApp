package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository
import kotlinx.coroutines.flow.Flow

class GetAllForwardPoliciesUseCase(private val forwardPolicyRepository: OTPForwardRepository) {
    operator fun invoke(): Flow<List<ForwardPolicyDetails>> =
        forwardPolicyRepository.getAllPolicies()
}