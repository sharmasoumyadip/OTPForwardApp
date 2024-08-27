package com.soumyadip.otpforwardapp.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.dto.FlowResult
import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import com.soumyadip.otpforwardapp.domain.dto.PolicyFilterAndContacts
import com.soumyadip.otpforwardapp.domain.dto.ValidationError
import com.soumyadip.otpforwardapp.domain.usecase.GetPolicyFilterAndContactsUseCase
import com.soumyadip.otpforwardapp.domain.usecase.SavePolicyWithContactsAndFiltersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class EditForwardPolicyViewModel @Inject constructor
    (
    private val getPolicyFilterAndContactsUseCase: GetPolicyFilterAndContactsUseCase,
    private val savePolicyWithContactsAndFiltersUseCase: SavePolicyWithContactsAndFiltersUseCase
) :
    ViewModel() {

    private var _savedAllPhoneContactsList: List<PhoneContact>? = null
    private var _savedFilterList: List<Filter>? = null

    private val _saveResult = MutableStateFlow<FlowResult<Boolean>>(FlowResult.Loading())
    val saveResult: StateFlow<FlowResult<Boolean>> get() = _saveResult

    private var _forwardPolicyDetails: ForwardPolicyDetails? = null
    val forwardPolicy get() = _forwardPolicyDetails!!


    private val _policyFilterAndContactsFlowResult =
        MutableStateFlow<FlowResult<PolicyFilterAndContacts>>(FlowResult.Loading())
    val policyFilterAndContactsFlowResult: StateFlow<FlowResult<PolicyFilterAndContacts>> get() = _policyFilterAndContactsFlowResult


    private val comparator = compareByDescending<PhoneContact> { it.id < 0 } // Negative IDs first
        .thenByDescending { it.isSelected } // `isSelected` first
        .thenBy {
            it.displayName ?: ""
        } // `displayName` in alphabetical order, considering null as empty
        .thenBy { it.id } // Positive IDs second


    fun setForwardPolicy(forwardPolicyDetails: ForwardPolicyDetails) {
        _forwardPolicyDetails = forwardPolicyDetails
    }


    fun fetchValues(
        allPhoneContactsListArg: List<PhoneContact>?,
        filterListArg: List<Filter>?
    ) {
        viewModelScope.launch {
            _policyFilterAndContactsFlowResult.emit(FlowResult.Loading())

            _savedAllPhoneContactsList = allPhoneContactsListArg ?: _savedAllPhoneContactsList
            _savedFilterList = filterListArg ?: _savedFilterList

            val policyFilterAndCondition = getPolicyFilterAndContactsUseCase(
                forwardPolicy.policyId,
                _savedAllPhoneContactsList, _savedFilterList
            )
            Collections.sort(policyFilterAndCondition.allPhoneContactsWithSelected, comparator)
            Collections.sort(policyFilterAndCondition.selectedContactList, comparator)
            _policyFilterAndContactsFlowResult.emit(FlowResult.Success(policyFilterAndCondition))
        }

    }

    fun savePolicy() {
        viewModelScope.launch {
            try {
                _saveResult.emit(FlowResult.Loading())
                savePolicyWithContactsAndFiltersUseCase(
                    forwardPolicy,
                    policyFilterAndContactsFlowResult.value.data!!
                )
                _saveResult.emit(FlowResult.Success(data = true))
            } catch (validationError: ValidationError) {
                var errorMessage = mutableListOf<String>()
                if (validationError.isForwardPolicyInvalid) errorMessage.add("Title length can not be less than 6 or blank")
                if (validationError.isFilterListInvalid) errorMessage.add("Atleast One Filter needs to be configured")
                if (validationError.isContactListInvalid) errorMessage.add("Atleast One Contact needs to be selected")
                _saveResult.emit(FlowResult.Error(message = errorMessage.joinToString(separator = "\n")))
            }


        }
    }


}