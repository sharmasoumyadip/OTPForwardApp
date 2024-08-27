package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.FilterField
import com.soumyadip.otpforwardapp.domain.dto.FilterOperator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CheckIfPolicyIsTriggered(private val getFiltersForPolicyUseCase: GetFiltersForPolicyUseCase) {
    suspend operator fun invoke(policyId: Long, senderId: String, messageBody: String): Boolean {
        val filtersList = getFiltersForPolicyUseCase(policyId)

        return withContext(Dispatchers.Default) {
            for (filter in filtersList) {

                val filterFieldValue =
                    if (filter.filterField == FilterField.SENDER_ID) senderId.uppercase() else messageBody.uppercase()
                val filterSavedValue = filter.filterValue.uppercase()
                when (filter.filterOperator) {

                    FilterOperator.EQUALS -> {
                        if (filterFieldValue != filterSavedValue) return@withContext false
                    }

                    FilterOperator.NOT_EQUALS -> {
                        if (filterFieldValue == filterSavedValue) return@withContext false
                    }

                    FilterOperator.CONTAINS -> {
                        if (!filterFieldValue.contains(filterSavedValue)) return@withContext false
                    }

                    FilterOperator.DOES_NOT_CONTAINS -> {
                        if (filterFieldValue.contains(filterSavedValue)) return@withContext false
                    }

                    FilterOperator.STARTS_WITH -> {
                        if (!filterFieldValue.startsWith(filterSavedValue)) return@withContext false
                    }

                    FilterOperator.DOES_NOT_STARTS_WITH -> {
                        if (filterFieldValue.startsWith(filterSavedValue)) return@withContext false
                    }

                    FilterOperator.ENDS_WITH -> {
                        if (!filterFieldValue.endsWith(filterSavedValue)) return@withContext false
                    }

                    FilterOperator.DOES_NOT_ENDS_WITH -> {
                        if (filterFieldValue.endsWith(filterSavedValue)) return@withContext false
                    }
                }
            }
            true

        }

    }
}