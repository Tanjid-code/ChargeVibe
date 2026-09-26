package com.tanjid.chargevibe

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class VibeAccessibilityService : AccessibilityService(), BatteryReceiver.BatteryEventListener {

    companion object {
        private const val TAG = "ChargeVibe_Access"

        @Volatile
        var isRunning = false
        var instance: VibeAccessibilityService? = null
    }

    private lateinit var overlayManager: OverlayManager
    private lateinit var settings: SettingsManager
    private var batteryReceiver: BroadcastReceiver? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var currentBatteryLevel = -1

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected: Accessibility Bind Active")

        instance = this
        overlayManager = OverlayManager(this)
        settings = SettingsManager(this)
        BatteryReceiver.listener = this

        registerBatteryReceiver()
        acquireWakeLock()

        isRunning = true

        // Display overlay immediately if currently plugged in
        if (isCharging()) {
            overlayManager.showOverlay(getBatteryLevel())
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // No action required here
    }

    override fun onInterrupt() {
        Log.d(TAG, "onInterrupt: Service Interrupted")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: Tearing down Accessibility components")

        overlayManager.hideOverlay()
        unregisterBatteryReceiver()
        releaseWakeLock()

        BatteryReceiver.listener = null
        isRunning = false
        instance = null

        super.onDestroy()
    }

    // Battery Event Listeners
    override fun onPowerConnected(batteryLevel: Int) {
        currentBatteryLevel = batteryLevel
        if (settings.isServiceActive) {
            overlayManager.showOverlay(batteryLevel)
        }
    }

    override fun onPowerDisconnected() {
        overlayManager.hideOverlay()
    }

    override fun onBatteryLevelChanged(level: Int, isCharging: Boolean) {
        currentBatteryLevel = level
        if (settings.isServiceActive && isCharging) {
            if (overlayManager.isShowing()) {
                overlayManager.updateOverlay(level)
            } else {
                overlayManager.showOverlay(level)
            }
        } else {
            overlayManager.hideOverlay()
        }
    }

    fun triggerUpdate() {
        if (isCharging() && settings.isServiceActive) {
            overlayManager.updateOverlay(getBatteryLevel())
        } else {
            overlayManager.hideOverlay()
        }
    }

    fun forceShowTest() {
        overlayManager.showOverlay(getBatteryLevel())
    }

    fun forceHideTest() {
        overlayManager.hideOverlay()
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
            Log.e(TAG, "registerBatteryReceiver: Failed", e)
        }
    }

    private fun unregisterBatteryReceiver() {
        batteryReceiver?.let {
            try {
                unregisterReceiver(it)
            } catch (e: Exception) {
                Log.e(TAG, "unregisterBatteryReceiver: Failed", e)
            }
        }
        batteryReceiver = null
    }

    private fun acquireWakeLock() {
        try {
            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ChargeVibe::WakeLock").apply {
                acquire(10 * 60 * 1000L)
            }
        } catch (e: Exception) {
            Log.e(TAG, "acquireWakeLock: Failed", e)
        }
    }

    private fun releaseWakeLock() {
        try {
            wakeLock?.let {
                if (it.isHeld) it.release()
            }
        } catch (e: Exception) {
            Log.e(TAG, "releaseWakeLock: Failed", e)
        }
        wakeLock = null
    }

    fun isCharging(): Boolean {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.isCharging
    }

    fun getBatteryLevel(): Int {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}