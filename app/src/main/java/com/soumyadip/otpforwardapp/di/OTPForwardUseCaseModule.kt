package com.soumyadip.otpforwardapp.di

import com.soumyadip.otpforwardapp.domain.OTPForwardSMSSender
import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository
import com.soumyadip.otpforwardapp.domain.usecase.CheckIfPolicyIsTriggered
import com.soumyadip.otpforwardapp.domain.usecase.DeletePolicyUseCase
import com.soumyadip.otpforwardapp.domain.usecase.GetAllContactsWithSelectedForPolicy
import com.soumyadip.otpforwardapp.domain.usecase.GetAllForwardPoliciesUseCase
import com.soumyadip.otpforwardapp.domain.usecase.GetAllPhoneContactsUseCase
import com.soumyadip.otpforwardapp.domain.usecase.GetFiltersForPolicyUseCase
import com.soumyadip.otpforwardapp.domain.usecase.GetOnlyActivePolicyUseCase
import com.soumyadip.otpforwardapp.domain.usecase.GetOnlySelectedPhoneContactsUseCase
import com.soumyadip.otpforwardapp.domain.usecase.GetPhoneContactsForPolicyUseCase
import com.soumyadip.otpforwardapp.domain.usecase.GetPolicyFilterAndContactsUseCase
import com.soumyadip.otpforwardapp.domain.usecase.ProcessSMSUseCase
import com.soumyadip.otpforwardapp.domain.usecase.SavePolicyWithContactsAndFiltersUseCase
import com.soumyadip.otpforwardapp.domain.usecase.SearchInPhoneContactListUseCase
import com.soumyadip.otpforwardapp.domain.usecase.UpdatePolicyUseCase
import com.soumyadip.otpforwardapp.domain.usecase.ValidateContactListUseCase
import com.soumyadip.otpforwardapp.domain.usecase.ValidateFilterValueUseCase
import com.soumyadip.otpforwardapp.domain.usecase.ValidateFiltersListUseCase
import com.soumyadip.otpforwardapp.domain.usecase.ValidateForwardPolicyUseCase
import com.soumyadip.otpforwardapp.domain.usecase.ValidatePolicyFilterAndContactsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object OTPForwardUseCaseModule {
    @Provides
    fun provideDeletePolicyUseCase(otpForwardRepository: OTPForwardRepository): DeletePolicyUseCase {
        return DeletePolicyUseCase(otpForwardRepository)
    }

    @Provides
    fun provideGetAllForwardPoliciesUseCase(otpForwardRepository: OTPForwardRepository): GetAllForwardPoliciesUseCase {
        return GetAllForwardPoliciesUseCase(otpForwardRepository)
    }

    @Provides
    fun provideGetAllPhoneContactsUseCase(otpForwardRepository: OTPForwardRepository): GetAllPhoneContactsUseCase {
        return GetAllPhoneContactsUseCase(otpForwardRepository)
    }

    @Provides
    fun provideGetFiltersForPolicyUseCase(otpForwardRepository: OTPForwardRepository): GetFiltersForPolicyUseCase {
        return GetFiltersForPolicyUseCase(otpForwardRepository)
    }

    @Provides
    fun provideGetOnlyActivePolicyUseCase(otpForwardRepository: OTPForwardRepository): GetOnlyActivePolicyUseCase {
        return GetOnlyActivePolicyUseCase(otpForwardRepository)
    }

    @Provides
    fun provideGetOnlySelectedPhoneContactsUseCase(): GetOnlySelectedPhoneContactsUseCase {
        return GetOnlySelectedPhoneContactsUseCase()
    }

    @Provides
    fun provideGetPhoneContactsForPolicyUseCase(otpForwardRepository: OTPForwardRepository): GetPhoneContactsForPolicyUseCase {
        return GetPhoneContactsForPolicyUseCase(otpForwardRepository)
    }

    @Provides
    fun provideGetPolicyFilterAndContactsUseCase(
        getFiltersForPolicyUseCase: GetFiltersForPolicyUseCase,
        getAllContactsWithSelectedForPolicy: GetAllContactsWithSelectedForPolicy,
        getOnlySelectedPhoneContactsUseCase: GetOnlySelectedPhoneContactsUseCase
    ): GetPolicyFilterAndContactsUseCase {
        return GetPolicyFilterAndContactsUseCase(
            getFiltersForPolicyUseCase,
            getAllContactsWithSelectedForPolicy,
            getOnlySelectedPhoneContactsUseCase
        )
    }

    @Provides
    fun provideGetAllContactsWithSelectedForPolicy(
        getAllPhoneContactsUseCase: GetAllPhoneContactsUseCase,
        getPhoneContactsForPolicyUseCase: GetPhoneContactsForPolicyUseCase
    ): GetAllContactsWithSelectedForPolicy {
        return GetAllContactsWithSelectedForPolicy(
            getAllPhoneContactsUseCase,
            getPhoneContactsForPolicyUseCase
        )
    }

    @Provides
    fun provideSavePolicyWithContactsAndFiltersUseCase(
        forwardPolicyRepository: OTPForwardRepository,
        validatePolicyFilterAndContactsUseCase: ValidatePolicyFilterAndContactsUseCase
    ): SavePolicyWithContactsAndFiltersUseCase {
        return SavePolicyWithContactsAndFiltersUseCase(
            forwardPolicyRepository,
            validatePolicyFilterAndContactsUseCase
        )
    }

    @Provides
    fun provideSearchInPhoneContactListUseCase(): SearchInPhoneContactListUseCase {
        return SearchInPhoneContactListUseCase()
    }

    @Provides
    fun provideUpdatePolicyUseCase(forwardPolicyRepository: OTPForwardRepository): UpdatePolicyUseCase {
        return UpdatePolicyUseCase(forwardPolicyRepository)
    }

    @Provides
    fun provideValidateContactListUseCase(): ValidateContactListUseCase {
        return ValidateContactListUseCase()
    }

    @Provides
    fun provideValidateFiltersListUseCase(): ValidateFiltersListUseCase {
        return ValidateFiltersListUseCase()
    }

    @Provides
    fun provideValidateFilterValueUseCase(): ValidateFilterValueUseCase {
        return ValidateFilterValueUseCase()
    }

    @Provides
    fun provideValidateForwardPolicyUseCase(): ValidateForwardPolicyUseCase {
        return ValidateForwardPolicyUseCase()
    }

    @Provides
    fun provideValidatePolicyFilterAndContactsUseCase(
        validateForwardPolicyUseCase: ValidateForwardPolicyUseCase,
        validateContactListUseCase: ValidateContactListUseCase,
        validateFiltersListUseCase: ValidateFiltersListUseCase
    ): ValidatePolicyFilterAndContactsUseCase {
        return ValidatePolicyFilterAndContactsUseCase(
            validateForwardPolicyUseCase,
            validateContactListUseCase,
            validateFiltersListUseCase
        )
    }

    @Provides
    fun provideCheckIfPolicyIsTriggered(getFiltersForPolicyUseCase: GetFiltersForPolicyUseCase): CheckIfPolicyIsTriggered {
        return CheckIfPolicyIsTriggered(getFiltersForPolicyUseCase)
    }

    @Provides
    fun provideProcessSMSUseCase(
        getOnlyActivePolicyUseCase: GetOnlyActivePolicyUseCase,
        checkIfPolicyIsTriggered: CheckIfPolicyIsTriggered,
        getContactListForwardPolicyUseCase: GetPhoneContactsForPolicyUseCase,
        otpForwardSMSSender: OTPForwardSMSSender
    ): ProcessSMSUseCase {
        return ProcessSMSUseCase(
            getOnlyActivePolicyUseCase,
            checkIfPolicyIsTriggered,
            getContactListForwardPolicyUseCase,
            otpForwardSMSSender
        )
    }
}