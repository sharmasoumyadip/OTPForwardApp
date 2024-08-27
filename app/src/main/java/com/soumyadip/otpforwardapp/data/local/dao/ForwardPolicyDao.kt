package com.soumyadip.otpforwardapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.soumyadip.otpforwardapp.data.local.entity.FilterEntity
import com.soumyadip.otpforwardapp.data.local.entity.ForwardPolicyDetailsEntity
import com.soumyadip.otpforwardapp.data.local.entity.PolicyContactEntity
import kotlinx.coroutines.flow.Flow
import kotlin.math.max

@Dao
interface ForwardPolicyDao {
    @Upsert
    suspend fun insertOrUpdatePolicy(forwardPolicyDetailsEntity: ForwardPolicyDetailsEntity): Long

    @Query("select * from forward_policies")
    fun getAllPolicies(): Flow<List<ForwardPolicyDetailsEntity>>

    @Delete
    suspend fun deletePolicy(forwardPolicyDetailsEntity: ForwardPolicyDetailsEntity)

    @Query("Select * from forward_policies where isActive = true")
    suspend fun getOnlyActivePolicy(): List<ForwardPolicyDetailsEntity>

    @Upsert
    suspend fun insertOrUpdateContacts(contactList: PolicyContactEntity): Long

    @Query("select * from policy_contacts where policyId = :policyId")
    suspend fun getAllContactsForPolicy(policyId: Long): PolicyContactEntity?

    @Query("delete from policy_contacts where policyId = :policyId")
    suspend fun deleteContacts(policyId: Long)


    @Query("select * from filter_conditions where policyId = :policyId")
    suspend fun getAllFiltersForPolicy(policyId: Long): List<FilterEntity>

    @Query("delete from filter_conditions where  policyId = :policyId")
    suspend fun deleteAllFilters(policyId: Long)

    @Upsert
    suspend fun insertOrUpdateFilters(filterEntityList: List<FilterEntity>)

    @Transaction
    suspend fun updateOrSavePolicyAndContactsAndFilters(
        forwardPolicyDetailsEntity: ForwardPolicyDetailsEntity,
        contactList: PolicyContactEntity,
        filterEntityList: List<FilterEntity>
    ): Long {
        var policyId = insertOrUpdatePolicy(forwardPolicyDetailsEntity)
        policyId = max(policyId, forwardPolicyDetailsEntity.policyId)
        contactList.policyId = policyId
        filterEntityList.map { it.policyId = policyId }
        deleteAllFilters(policyId)
        insertOrUpdateContacts(contactList)
        insertOrUpdateFilters(filterEntityList)
        return policyId
    }


}