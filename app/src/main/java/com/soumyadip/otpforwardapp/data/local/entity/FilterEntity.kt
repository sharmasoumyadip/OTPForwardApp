package com.soumyadip.otpforwardapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.soumyadip.otpforwardapp.domain.dto.FilterField
import com.soumyadip.otpforwardapp.domain.dto.FilterOperator

@Entity(
    tableName = "filter_conditions",
    foreignKeys = [
        ForeignKey(
            entity = ForwardPolicyDetailsEntity::class,
            parentColumns = ["policyId"],
            childColumns = ["policyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FilterEntity(
    @PrimaryKey(autoGenerate = true)
    val filterId: Long = 0L,
    var policyId: Long = 0L,
    val filterField: FilterField,
    val filterOperator: FilterOperator,
    val filterValue: String
)