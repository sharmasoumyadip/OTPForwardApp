package com.soumyadip.otpforwardapp.data

import com.soumyadip.otpforwardapp.data.local.entity.FilterEntity
import com.soumyadip.otpforwardapp.data.local.entity.ForwardPolicyDetailsEntity
import com.soumyadip.otpforwardapp.data.phonecontact.model.PhoneContactEntity
import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import kotlin.math.max


fun phoneContactEntityToDTO(phoneContactEntity: PhoneContactEntity): PhoneContact {

    return PhoneContact(
        id = phoneContactEntity.id.toLong(),
        displayName = phoneContactEntity.displayName,
        phoneNumber = phoneContactEntity.phoneNumber
    )
}

fun forwardPolicyDetailsEntityToDTO(forwardPolicyDetailsEntity: ForwardPolicyDetailsEntity): ForwardPolicyDetails {

    return ForwardPolicyDetails(
        policyId = forwardPolicyDetailsEntity.policyId,
        policyName = forwardPolicyDetailsEntity.policyName,
        uniqueMessageIdentifier = forwardPolicyDetailsEntity.uniqueMessageIdentifier,
        isActive = forwardPolicyDetailsEntity.isActive
    )
}

fun forwardPolicyDetailsDTOToEntity(forwardPolicyDetails: ForwardPolicyDetails): ForwardPolicyDetailsEntity {
    return ForwardPolicyDetailsEntity(
        policyId = max(0L, forwardPolicyDetails.policyId),
        policyName = forwardPolicyDetails.policyName,
        uniqueMessageIdentifier = forwardPolicyDetails.uniqueMessageIdentifier,
        isActive = forwardPolicyDetails.isActive
    )
}

fun filterEntityToDTO(filterEntity: FilterEntity): Filter {
    return Filter(
        filterEntity.filterId,
        filterEntity.policyId,
        filterEntity.filterField,
        filterEntity.filterOperator,
        filterEntity.filterValue
    )

}

fun filterDTOtoEntity(filter: Filter): FilterEntity {
    return FilterEntity(
        filterId = max(filter.filterId, 0L),
        filterField = filter.filterField,
        filterOperator = filter.filterOperator,
        filterValue = filter.filterValue
    )
}

