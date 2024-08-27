package com.soumyadip.otpforwardapp.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.dto.FlowResult
import com.soumyadip.otpforwardapp.domain.dto.ValidationError
import com.soumyadip.otpforwardapp.domain.usecase.ValidateFilterValueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigureFiltersViewModel @Inject constructor(
    private val validateFilterValueUseCase: ValidateFilterValueUseCase
) : ViewModel() {
    private var _filterList: MutableList<Filter>? = null
    private val _filterListValidationResult = MutableStateFlow<FlowResult<Boolean>?>(null)
    val filterListValidationResult: StateFlow<FlowResult<Boolean>?> get() = _filterListValidationResult
    val filterList: List<Filter> get() = _filterList!!
    fun setValue(filterListArg: List<Filter>) {
        if (_filterList == null) _filterList = filterListArg.toMutableList()
    }

    fun addFilter(filter: Filter) = _filterList!!.add(filter)
    fun removeFilter(position: Int) = _filterList!!.removeAt(position)

    fun validateFilterList() {
        viewModelScope.launch {

            _filterListValidationResult.emit(FlowResult.Loading())
            //delay(5000)
            try {
                validateFilterValueUseCase(filterList)
                _filterListValidationResult.emit(FlowResult.Success(true))
            } catch (e: ValidationError) {
                _filterListValidationResult.emit(FlowResult.Error(message = "Filter Value can not be empty."))
            }

        }
    }
}