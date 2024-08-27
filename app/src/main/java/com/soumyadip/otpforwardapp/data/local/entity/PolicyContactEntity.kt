package com.soumyadip.otpforwardapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "policy_contacts",
    foreignKeys = [
        ForeignKey(
            entity = ForwardPolicyDetailsEntity::class,
            parentColumns = ["policyId"],
            childColumns = ["policyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PolicyContactEntity(
    @PrimaryKey(autoGenerate = false)
    var policyId: Long = 0L,
    val contactsList: List<String>
)
