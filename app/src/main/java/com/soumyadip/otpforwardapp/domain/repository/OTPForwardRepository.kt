package com.soumyadip.otpforwardapp.domain.repository

import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import kotlinx.coroutines.flow.Flow

interface OTPForwardRepository {
    suspend fun getAllPhoneContacts(): List<PhoneContact>
    fun getAllPolicies(): Flow<List<ForwardPolicyDetails>>
    suspend fun getSavedContactsForPolicy(policyId: Long): List<String>

    suspend fun getSavedFiltersForPolicy(policyId: Long): List<Filter>
    suspend fun saveForwardPolicy(
        forwardPolicyDetails: ForwardPolicyDetails,
        phoneContactList: List<PhoneContact>,
        filterList: List<Filter>
    )

    suspend fun deletePolicy(policyDetails: ForwardPolicyDetails)
    suspend fun updatePolicy(policyDetails: ForwardPolicyDetails)
    suspend fun getOnlyActivePolicies(): List<ForwardPolicyDetails>
}