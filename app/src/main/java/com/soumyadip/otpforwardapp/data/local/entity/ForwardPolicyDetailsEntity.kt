package com.soumyadip.otpforwardapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forward_policies")
data class ForwardPolicyDetailsEntity(
    @PrimaryKey(autoGenerate = true)
    val policyId: Long = 0L,
    val policyName: String,
    val uniqueMessageIdentifier: String? = null,
    val isActive: Boolean = false
)
