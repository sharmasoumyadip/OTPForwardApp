package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.dto.ValidationError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ValidateFilterValueUseCase() {
    suspend operator fun invoke(filterList: List<Filter>): Boolean {
        return withContext(Dispatchers.Default) {
            for (filter in filterList) if (filter.filterValue.trim()
                    .isBlank()
            ) throw ValidationError(isFilterListInvalid = true)
            true
        }
    }
}