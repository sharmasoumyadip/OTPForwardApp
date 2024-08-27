package com.soumyadip.otpforwardapp.domain.dto

data class Filter(
    val filterId: Long = 0L,
    val policyId: Long,
    var filterField: FilterField = FilterField.SENDER_ID,
    var filterOperator: FilterOperator = FilterOperator.EQUALS,
    var filterValue: String = ""
)