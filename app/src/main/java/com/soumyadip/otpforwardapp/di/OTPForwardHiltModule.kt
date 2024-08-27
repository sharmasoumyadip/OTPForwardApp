package com.soumyadip.otpforwardapp.di

import android.content.ContentResolver
import android.content.Context
import android.telephony.SmsManager
import androidx.room.Room
import com.google.gson.Gson
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.soumyadip.otpforwardapp.data.local.ForwardPolicyDatabase
import com.soumyadip.otpforwardapp.data.local.dao.ForwardPolicyDao
import com.soumyadip.otpforwardapp.data.phonecontact.api.PhoneContactDAO
import com.soumyadip.otpforwardapp.data.repository.OTPForwardRepositoryImpl
import com.soumyadip.otpforwardapp.domain.OTPForwardSMSSender
import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository
import com.soumyadip.otpforwardapp.infrastructure.OTPForwardSMSSenderImpl
import com.soumyadip.otpforwardapp.infrastructure.PhoneContactDAOImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OTPForwardHiltModule {
    @Provides
    @Singleton
    fun providesContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun providesPhoneContactDAO(contentResolver: ContentResolver): PhoneContactDAO {
        return PhoneContactDAOImpl(contentResolver)
    }

    @Provides
    @Singleton
    fun providesForwardPolicyDatabase(@ApplicationContext context: Context): ForwardPolicyDatabase {
        return Room.databaseBuilder(
            context,
            ForwardPolicyDatabase::class.java,
            "FORWARD_POLICY_DATABASE"
        ).build()
    }

    @Provides
    @Singleton
    fun providesForwardPolicyDAO(forwardPolicyDatabase: ForwardPolicyDatabase): ForwardPolicyDao {
        return forwardPolicyDatabase.getForwardPolicyDao()
    }


    @Provides
    @Singleton
    fun providesOTPForwardRepositary(
        phoneContactDAO: PhoneContactDAO, phoneNumberUtil: PhoneNumberUtil,
        forwardPolicyDao: ForwardPolicyDao
    ): OTPForwardRepository {
        return OTPForwardRepositoryImpl(phoneContactDAO, phoneNumberUtil, forwardPolicyDao)
    }

    @Provides
    @Singleton
    fun providesPhoneUtil(): PhoneNumberUtil {
        return PhoneNumberUtil.getInstance()
    }

    @Provides
    @Singleton
    fun providesGSON(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideSMSManager(@ApplicationContext context: Context): SmsManager {
        return context.getSystemService(SmsManager::class.java)
    }

    @Provides
    fun provideOTPForwardSMSSender(smsManager: SmsManager): OTPForwardSMSSender {
        return OTPForwardSMSSenderImpl(smsManager)
    }


}