package com.soumyadip.otpforwardapp.presentation

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.provider.Telephony
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.soumyadip.otpforwardapp.R
import com.soumyadip.otpforwardapp.domain.usecase.ProcessSMSUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class OTPForwardService : Service() {
    private val NOTIFICATION_ID = 8201

    @Inject
    lateinit var processSMSUseCase: ProcessSMSUseCase
    private lateinit var smsReceiver: NewSMSReceiver
    private val serviceScope = CoroutineScope(SupervisorJob())

    private var isRunning = false

    companion object {
        private var _serviceRunningSince = ""
        val serviceRunningSince get() = _serviceRunningSince
        private val _isServiceRunning = MutableStateFlow<Boolean>(false)
        val isServiceRunning: StateFlow<Boolean> = _isServiceRunning
    }


    enum class Action {
        Start_Service,
        Stop_Service,
        Start_Foreground_Service_Only
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Action.Start_Service.toString() -> {

                smsReceiver = NewSMSReceiver(::processSMS)
                val smsIntentFilter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
                val currentDateTime: LocalDateTime = LocalDateTime.now()
                val formatter =
                    DateTimeFormatter.ofPattern("dd - MMM - YYYY HH:mm a") // 'a' for AM/PM
                _serviceRunningSince = currentDateTime.format(formatter)
                registerReceiver(smsReceiver, smsIntentFilter)
                startForegroundService()
                _isServiceRunning.value = true
            }

            Action.Stop_Service.toString() -> {
                if (_isServiceRunning.value) stopSelf()
            }

            Action.Start_Foreground_Service_Only.toString() -> {
                startForegroundService()

            }


        }
        return START_STICKY
    }


    override fun onDestroy() {
        _isServiceRunning.value = false
        serviceScope.cancel()
        unregisterReceiver(smsReceiver)
        stopForeground(STOP_FOREGROUND_DETACH)
        super.onDestroy()

    }

    private fun processSMS(senderId: String, messageBody: String) {
        serviceScope.launch {
            processSMSUseCase(senderId, messageBody)
        }
    }

    private fun startForegroundService() {
        val notificationLayoutExpanded =
            RemoteViews(packageName, R.layout.notificaction_layout_expanded)

        notificationLayoutExpanded.setTextViewText(
            R.id.notificaton_service_running_since,
            "Service Running Since: ${serviceRunningSince}"
        )


        val startOnlyForegroundServiceIntent =
            Intent(applicationContext, OTPForwardService::class.java).apply {
                action = OTPForwardService.Action.Start_Foreground_Service_Only.toString()
            }
        val onDeletePendingIntent = PendingIntent.getService(
            applicationContext, NOTIFICATION_ID * 1, startOnlyForegroundServiceIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val stopServiceIntent = Intent(applicationContext, OTPForwardService::class.java).apply {
            action = Action.Stop_Service.toString()
        }
        val stopServicePendingIntent = PendingIntent.getService(
            applicationContext, NOTIFICATION_ID * 2, stopServiceIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        notificationLayoutExpanded.setOnClickPendingIntent(
            R.id.notification_stop_button, stopServicePendingIntent
        )

        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)
        val mainActivityPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID * 3,
            mainActivityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(this, "running_channel")
            .setSmallIcon(R.drawable.otp_icon)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentTitle("OTP Forward Service is Running")
            .setCustomBigContentView(notificationLayoutExpanded)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)
            .setDeleteIntent(onDeletePendingIntent)
            .setContentIntent(mainActivityPendingIntent)
            .setAutoCancel(true)
            .build()
        startForeground(
            NOTIFICATION_ID, notification, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else {
                0
            }
        )
    }


}