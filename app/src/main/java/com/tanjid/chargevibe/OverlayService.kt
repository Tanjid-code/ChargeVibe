package com.tanjid.chargevibe

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class OverlayService : Service(), BatteryReceiver.BatteryEventListener {

    companion object {
        private const val TAG = "ChargeVibe_Service"
        const val CHANNEL_ID = "chargevibe_service_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_STOP = "com.tanjid.chargevibe.STOP"
        const val ACTION_UPDATE = "com.tanjid.chargevibe.UPDATE"

        @Volatile
        var isRunning = false
        var instance: OverlayService? = null
    }

    private lateinit var overlayManager: OverlayManager
    private lateinit var settings: SettingsManager
    private var batteryReceiver: BroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        instance = this
        overlayManager = OverlayManager(this)
        settings = SettingsManager(this)
        BatteryReceiver.listener = this

        createNotificationChannel()
        registerBatteryReceiver()

        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: action = ${intent?.action}")

        // Start foreground FIRST — required by Android 8+
        val notification = createNotification(getBatteryLevel(), isCharging())
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed startForeground", e)
            try {
                startForeground(NOTIFICATION_ID, notification)
            } catch (_: Exception) {}
        }

        when (intent?.action) {
            ACTION_STOP -> {
                stopSelf()
                return START_NOT_STICKY
            }
            ACTION_UPDATE -> {
                // KEY FIX: Always update the overlay when settings change, regardless of charging state
                refreshOverlay()
                return START_STICKY
            }
        }

        // Initial start: show if "always show" or currently charging
        refreshOverlay()
        return START_STICKY
    }

    /**
     * SINGLE SOURCE OF TRUTH: Decides whether to show, update or hide the overlay
     * based on current settings and charging state.
     */
    private fun refreshOverlay() {
        val charging = isCharging()
        val level = getBatteryLevel()

        val shouldShow = settings.alwaysShow || charging

        if (shouldShow) {
            if (overlayManager.isShowing()) {
                overlayManager.updateOverlay(level)
            } else {
                overlayManager.showOverlay(level)
            }
        } else {
            overlayManager.hideOverlay()
        }

        updateNotification(level, charging)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        overlayManager.hideOverlay()
        unregisterBatteryReceiver()

        BatteryReceiver.listener = null
        isRunning = false
        instance = null

        // Auto-restart if user intended it to be active
        if (settings.isServiceActive) {
            val restartIntent = Intent(this, OverlayService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(restartIntent)
            } else {
                startService(restartIntent)
            }
        }
        super.onDestroy()
    }

    override fun onPowerConnected(batteryLevel: Int) {
        refreshOverlay()
    }

    override fun onPowerDisconnected() {
        refreshOverlay()
    }

    override fun onBatteryLevelChanged(level: Int, isCharging: Boolean) {
        refreshOverlay()
    }

    private fun registerBatteryReceiver() {
        if (batteryReceiver != null) return
        batteryReceiver = BatteryReceiver()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(batteryReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
            } else {
                registerReceiver(batteryReceiver, filter)
            }
        } catch (e: Exception) {
            Log.e(TAG, "registerReceiver error", e)
        }
    }

    private fun unregisterBatteryReceiver() {
        batteryReceiver?.let {
            try {
                unregisterReceiver(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        batteryReceiver = null
    }

    private fun isCharging(): Boolean {
        val bm = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return bm.isCharging
    }

    private fun getBatteryLevel(): Int {
        val bm = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notification_channel_desc)
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(level: Int, isCharging: Boolean): Notification {
        val text = when {
            isCharging && level >= 0 -> "⚡ Charging: $level%"
            level >= 0 -> "🔋 Battery: $level%"
            else -> getString(R.string.notification_text)
        }

        val launchIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, OverlayService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 1, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_delete, "Stop", stopPendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun updateNotification(level: Int, isCharging: Boolean) {
        val notification = createNotification(level, isCharging)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }
}