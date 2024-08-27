package com.soumyadip.otpforwardapp.infrastructure

import android.content.ContentResolver
import android.provider.ContactsContract
import com.soumyadip.otpforwardapp.data.phonecontact.api.PhoneContactDAO
import com.soumyadip.otpforwardapp.data.phonecontact.model.PhoneContactEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhoneContactDAOImpl(private val contentResolver: ContentResolver) : PhoneContactDAO {
    override suspend fun getAllPhoneContacts(): List<PhoneContactEntity> {
        return withContext(Dispatchers.IO) {
            _getAllPhoneContacts()
        }
    }

    private fun _getAllPhoneContacts(): List<PhoneContactEntity> {


        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        val phoneContactEntityList = mutableListOf<PhoneContactEntity>()
        cursor?.use {
            val idIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)
            val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                val id = it.getString(idIdx).toInt()
                val name = it.getString(nameIdx)
                val number = it.getString(numberIdx)
                phoneContactEntityList.add(PhoneContactEntity(id, name, number))
            }

        }
        return phoneContactEntityList

    }
}