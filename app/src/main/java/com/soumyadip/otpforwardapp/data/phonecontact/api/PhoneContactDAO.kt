package com.soumyadip.otpforwardapp.data.phonecontact.api

import com.soumyadip.otpforwardapp.data.phonecontact.model.PhoneContactEntity

interface PhoneContactDAO {
    suspend fun getAllPhoneContacts(): List<PhoneContactEntity>
}