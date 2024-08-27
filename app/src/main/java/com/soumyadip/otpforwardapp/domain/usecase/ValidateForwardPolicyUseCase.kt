package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails

class ValidateForwardPolicyUseCase() {
    operator fun invoke(forwardPolicyDetails: ForwardPolicyDetails): Boolean {
        return forwardPolicyDetails.policyName.isNotBlank() && forwardPolicyDetails.policyName.length >= 8
    }
}