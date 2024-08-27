package com.soumyadip.otpforwardapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.soumyadip.otpforwardapp.data.local.dao.ForwardPolicyDao
import com.soumyadip.otpforwardapp.data.local.entity.FilterEntity
import com.soumyadip.otpforwardapp.data.local.entity.ForwardPolicyDetailsEntity
import com.soumyadip.otpforwardapp.data.local.entity.PolicyContactEntity

@Database
    (
    entities = [ForwardPolicyDetailsEntity::class, PolicyContactEntity::class, FilterEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ForwardPolicyDatabase : RoomDatabase() {
    abstract fun getForwardPolicyDao(): ForwardPolicyDao
}