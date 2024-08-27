package com.soumyadip.otpforwardapp.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soumyadip.otpforwardapp.domain.dto.FlowResult
import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.usecase.DeletePolicyUseCase
import com.soumyadip.otpforwardapp.domain.usecase.GetAllForwardPoliciesUseCase
import com.soumyadip.otpforwardapp.domain.usecase.UpdatePolicyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class ForwardPolicyMainViewModel @Inject constructor(
    private val getAllForwardPoliciesUseCase: GetAllForwardPoliciesUseCase,
    private val deletePolicyUseCase: DeletePolicyUseCase,
    private val updatePolicyUseCase: UpdatePolicyUseCase
) : ViewModel() {
    private val _forwardPolicyDetailsListResult =
        MutableStateFlow<FlowResult<List<ForwardPolicyDetails>>>(FlowResult.Loading())
    val forwardPolicyDetailsListResult: StateFlow<FlowResult<List<ForwardPolicyDetails>>> get() = _forwardPolicyDetailsListResult

    init {
        viewModelScope.launch {
            _forwardPolicyDetailsListResult.emit(FlowResult.Loading())

            getAllForwardPoliciesUseCase().catch {
                _forwardPolicyDetailsListResult.emit(FlowResult.Error(it.message.toString()))
            }.collect { policyList ->
                Collections.sort(policyList,
                    compareBy<ForwardPolicyDetails> { !it.isActive }.thenBy { it.policyId })
                _forwardPolicyDetailsListResult.emit(FlowResult.Success(policyList))
            }


        }
    }

    fun deletePolicy(policyDetails: ForwardPolicyDetails) {
        viewModelScope.launch {
            deletePolicyUseCase(policyDetails)
        }
    }

    fun updatePolicy(policyDetails: ForwardPolicyDetails) {
        viewModelScope.launch {
            updatePolicyUseCase(policyDetails)
        }
    }

}