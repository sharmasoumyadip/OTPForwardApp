package com.soumyadip.otpforwardapp.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soumyadip.otpforwardapp.domain.dto.FlowResult
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact
import com.soumyadip.otpforwardapp.domain.usecase.SearchInPhoneContactListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class ContactPickerViewModel @Inject constructor(
    private val searchInPhoneContactListUseCase: SearchInPhoneContactListUseCase
) : ViewModel() {
    private var _phoneContactList: List<PhoneContact>? = null
    val phoneContactList get() = _phoneContactList!!
    private val _filteredContactList =
        MutableStateFlow<FlowResult<List<PhoneContact>>>(FlowResult.Loading())
    val filteredContactList: StateFlow<FlowResult<List<PhoneContact>>> get() = _filteredContactList
    private val comparator = compareByDescending<PhoneContact> { it.id < 0 } // Negative IDs first
        .thenByDescending { it.isSelected } // `isSelected` first
        .thenBy {
            it.displayName ?: ""
        } // `displayName` in alphabetical order, considering null as empty
        .thenBy { it.id } // Positive IDs second

    fun setValue(list: List<PhoneContact>) {
        if (_phoneContactList == null) _phoneContactList = list
    }

    fun fetchFilteredContacts(query: String?) {
        viewModelScope.launch {
            try {
                _filteredContactList.emit(FlowResult.Loading())
                val filteredContacts =
                    searchInPhoneContactListUseCase(query, _phoneContactList ?: emptyList())
                Collections.sort(filteredContacts, comparator)
                _filteredContactList.emit(FlowResult.Success(data = filteredContacts))
            } catch (e: Exception) {

                _filteredContactList.emit(FlowResult.Error(message = e.message))
            }
        }

    }
}
