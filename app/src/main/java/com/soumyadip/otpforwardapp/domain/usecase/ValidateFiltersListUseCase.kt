package com.soumyadip.otpforwardapp.domain.usecase

import com.soumyadip.otpforwardapp.domain.dto.Filter

class ValidateFiltersListUseCase() {
    operator fun invoke(filterList: List<Filter>): Boolean {
        return filterList.isNotEmpty()
    }

}