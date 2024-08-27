package com.soumyadip.otpforwardapp.domain.dto

enum class FilterOperator(val stringValue: String) {
    EQUALS("EQUALS"),
    NOT_EQUALS("NOT EQUALS"),
    CONTAINS("CONTAINS"),
    DOES_NOT_CONTAINS("DOES NOT CONTAINS"),
    STARTS_WITH("STARTS WITH"),
    DOES_NOT_STARTS_WITH("DOES NOT STARTS WITH"),
    ENDS_WITH("ENDS WITH"),
    DOES_NOT_ENDS_WITH("DOES NOT ENDS WITH")

}