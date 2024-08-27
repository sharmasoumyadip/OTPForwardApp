package com.soumyadip.otpforwardapp.domain.dto

data class PolicyFilterAndContacts(
    val policyId: Long = 0,
    val allPhoneContactsWithSelected: List<PhoneContact>,
    val selectedContactList: List<PhoneContact>,
    val savedFilters: List<Filter>
)
