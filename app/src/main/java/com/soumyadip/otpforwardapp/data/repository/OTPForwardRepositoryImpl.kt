package com.soumyadip.otpforwardapp.data.repository

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.soumyadip.otpforwardapp.data.filterDTOtoEntity
import com.soumyadip.otpforwardapp.data.filterEntityToDTO
import com.soumyadip.otpforwardapp.data.forwardPolicyDetailsDTOToEntity
import com.soumyadip.otpforwardapp.data.forwardPolicyDetailsEntityToDTO
import com.soumyadip.otpforwardapp.data.local.dao.ForwardPolicyDao
import com.soumyadip.otpforwardapp.data.local.entity.PolicyContactEntity
import com.soumyadip.otpforwardapp.data.phoneContactEntityToDTO
import com.soumyadip.otpforwardapp.data.phonecontact.api.PhoneContactDAO
import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class OTPForwardRepositoryImpl(
    private val phoneContactDAO: PhoneContactDAO,
    private val phoneNumberUtil: PhoneNumberUtil,
    private val forwardPolicyDao: ForwardPolicyDao
) : OTPForwardRepository {


    override suspend fun getAllPhoneContacts(): List<PhoneContact> {
        return phoneContactDAO.getAllPhoneContacts().mapNotNull {
            try {
                val phoneNumber = phoneNumberUtil.parse(it.phoneNumber, "IN")
                it.phoneNumber =
                    phoneNumberUtil.format(
                        phoneNumber,
                        PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
                    )

                phoneContactEntityToDTO(it)
            } catch (e: NumberParseException) {
                null

            }
        }
    }

    override suspend fun deletePolicy(policyDetails: ForwardPolicyDetails) {
        forwardPolicyDao.deletePolicy(forwardPolicyDetailsDTOToEntity(policyDetails))
    }

    override suspend fun updatePolicy(policyDetails: ForwardPolicyDetails) {
        forwardPolicyDao.insertOrUpdatePolicy(forwardPolicyDetailsDTOToEntity(policyDetails))
    }

    override fun getAllPolicies(): Flow<List<ForwardPolicyDetails>> {

        return forwardPolicyDao.getAllPolicies().map { forwardPolicyEntituList ->
            forwardPolicyEntituList.map {
                forwardPolicyDetailsEntityToDTO(it)
            }
        }

    }

    override suspend fun getOnlyActivePolicies(): List<ForwardPolicyDetails> {
        return forwardPolicyDao.getOnlyActivePolicy().map {
            forwardPolicyDetailsEntityToDTO(it)
        }

    }

    override suspend fun getSavedContactsForPolicy(policyId: Long): List<String> {
        return forwardPolicyDao.getAllContactsForPolicy(policyId)?.contactsList ?: emptyList()
    }

    override suspend fun getSavedFiltersForPolicy(policyId: Long): List<Filter> {
        return forwardPolicyDao.getAllFiltersForPolicy(policyId)
            .map { filterEntityToDTO(it) }
    }

    override suspend fun saveForwardPolicy(
        forwardPolicyDetails: ForwardPolicyDetails,
        phoneContactList: List<PhoneContact>,
        filterList: List<Filter>
    ) {
        val forwardPolicyDetailsEntity = forwardPolicyDetailsDTOToEntity(forwardPolicyDetails)
        val phoneContactEntity =
            PolicyContactEntity(contactsList = phoneContactList.map { it.phoneNumber })
        val filterEntityList = filterList.map { filterDTOtoEntity(it) }
        forwardPolicyDao.updateOrSavePolicyAndContactsAndFilters(
            forwardPolicyDetailsEntity,
            phoneContactEntity, filterEntityList
        )
    }
}

