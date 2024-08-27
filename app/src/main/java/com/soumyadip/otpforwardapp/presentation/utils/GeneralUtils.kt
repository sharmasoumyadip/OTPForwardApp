package com.soumyadip.otpforwardapp.presentation.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.soumyadip.otpforwardapp.domain.dto.Filter
import com.soumyadip.otpforwardapp.domain.dto.ForwardPolicyDetails
import com.soumyadip.otpforwardapp.domain.dto.PhoneContact

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)

}

fun getNewForwardPolicy(): ForwardPolicyDetails {
    return ForwardPolicyDetails(
        policyId = System.currentTimeMillis() * -1L,
        policyName = "New Forward Policy"

    )
}

fun getNewContactList(): MutableList<PhoneContact> = mutableListOf()

fun getNewFilterCondition(): Filter {
    return Filter(System.currentTimeMillis() * -1, 0)
}