package com.tanjid.chargevibe

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class BatteryReceiver : BroadcastReceiver() {

    companion object {
        var listener: BatteryEventListener? = null
    }

    interface BatteryEventListener {
        fun onPowerConnected(batteryLevel: Int)
        fun onPowerDisconnected()
        fun onBatteryLevelChanged(level: Int, isCharging: Boolean)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                val level = getBatteryLevel(context)
                listener?.onPowerConnected(level)
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                listener?.onPowerDisconnected()
            }
            Intent.ACTION_BATTERY_CHANGED -> {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
                val batteryPct = (level * 100) / scale
                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL
                listener?.onBatteryLevelChanged(batteryPct, isCharging)
            }
        }
    }

    private fun getBatteryLevel(context: Context): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}