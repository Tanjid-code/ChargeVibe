package com.tanjid.chargevibe

import android.content.Context
import android.content.SharedPreferences
import com.tanjid.chargevibe.model.DesignType

class SettingsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("chargevibe_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SELECTED_DESIGN_ID = "selected_design_id"
        private const val KEY_DESIGN_TYPE = "design_type"
        private const val KEY_DRAWABLE_RES = "drawable_res"
        private const val KEY_EMOJI = "emoji"
        private const val KEY_CUSTOM_IMAGE_PATH = "custom_image_path"
        private const val KEY_HEIGHT = "overlay_height_dp"
        private const val KEY_WIDTH = "overlay_width_dp"
        private const val KEY_TRANSPARENCY = "transparency"
        private const val KEY_ANIM_SPEED = "anim_speed"
        private const val KEY_POS_X = "pos_x_px"
        private const val KEY_POS_Y = "pos_y_px"
        private const val KEY_SERVICE_ACTIVE = "service_active"
        private const val KEY_ALWAYS_SHOW = "always_show"
        private const val KEY_MASK_COLOR = "mask_color"
        private const val KEY_IS_DRAG_MODE = "is_drag_mode"
        private const val KEY_ENABLE_ANIMATION = "enable_animation"

        private const val KEY_DEVICE_PRESET_ID = "device_preset_id"
        private const val KEY_SHOW_ON_LOCK_SCREEN = "show_on_lock_screen"
        private const val KEY_BATTERY_OPT_PROMPT_DISMISSED = "battery_opt_prompt_dismissed"

        // NEW: battery-health reactive tint toggle
        private const val KEY_BATTERY_COLOR_REACTIVE = "battery_color_reactive"
    }

    var selectedDesignId: Int
        get() = prefs.getInt(KEY_SELECTED_DESIGN_ID, 0)
        set(value) = prefs.edit().putInt(KEY_SELECTED_DESIGN_ID, value).apply()

    var designType: DesignType
        get() = try {
            DesignType.valueOf(
                prefs.getString(KEY_DESIGN_TYPE, DesignType.BUILTIN_DRAWABLE.name)
                    ?: DesignType.BUILTIN_DRAWABLE.name
            )
        } catch (e: Exception) {
            DesignType.BUILTIN_DRAWABLE
        }
        set(value) = prefs.edit().putString(KEY_DESIGN_TYPE, value.name).apply()

    var drawableRes: Int
        get() = prefs.getInt(KEY_DRAWABLE_RES, R.drawable.ic_bolt)
        set(value) = prefs.edit().putInt(KEY_DRAWABLE_RES, value).apply()

    var emoji: String
        get() = prefs.getString(KEY_EMOJI, "⚡") ?: "⚡"
        set(value) = prefs.edit().putString(KEY_EMOJI, value).apply()

    var customImagePath: String?
        get() = prefs.getString(KEY_CUSTOM_IMAGE_PATH, null)
        set(value) = prefs.edit().putString(KEY_CUSTOM_IMAGE_PATH, value).apply()

    var overlayHeightDp: Float
        get() = prefs.getFloat(KEY_HEIGHT, 30f).coerceIn(10f, 150f)
        set(value) = prefs.edit().putFloat(KEY_HEIGHT, value.coerceIn(10f, 150f)).apply()

    var overlayWidthDp: Float
        get() = prefs.getFloat(KEY_WIDTH, 60f).coerceIn(20f, 250f)
        set(value) = prefs.edit().putFloat(KEY_WIDTH, value.coerceIn(20f, 250f)).apply()

    var transparency: Float
        get() = prefs.getFloat(KEY_TRANSPARENCY, 1.0f).coerceIn(0.1f, 1.0f)
        set(value) = prefs.edit().putFloat(KEY_TRANSPARENCY, value.coerceIn(0.1f, 1.0f)).apply()

    var animSpeed: Float
        get() = prefs.getFloat(KEY_ANIM_SPEED, 1500f).coerceIn(300f, 4000f)
        set(value) = prefs.edit().putFloat(KEY_ANIM_SPEED, value.coerceIn(300f, 4000f)).apply()

    var posX: Int
        get() = prefs.getInt(KEY_POS_X, -9999)
        set(value) = prefs.edit().putInt(KEY_POS_X, value).apply()

    var posY: Int
        get() = prefs.getInt(KEY_POS_Y, 0)
        set(value) = prefs.edit().putInt(KEY_POS_Y, value).apply()

    var isServiceActive: Boolean
        get() = prefs.getBoolean(KEY_SERVICE_ACTIVE, false)
        set(value) = prefs.edit().putBoolean(KEY_SERVICE_ACTIVE, value).apply()

    var alwaysShow: Boolean
        get() = prefs.getBoolean(KEY_ALWAYS_SHOW, true)
        set(value) = prefs.edit().putBoolean(KEY_ALWAYS_SHOW, value).apply()

    var maskColor: Int
        get() = prefs.getInt(KEY_MASK_COLOR, 0)
        set(value) = prefs.edit().putInt(KEY_MASK_COLOR, value).apply()

    var isDragMode: Boolean
        get() = prefs.getBoolean(KEY_IS_DRAG_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_DRAG_MODE, value).apply()

    var enableAnimation: Boolean
        get() = prefs.getBoolean(KEY_ENABLE_ANIMATION, true)
        set(value) = prefs.edit().putBoolean(KEY_ENABLE_ANIMATION, value).apply()

    var devicePresetId: String
        get() = prefs.getString(KEY_DEVICE_PRESET_ID, "auto") ?: "auto"
        set(value) = prefs.edit().putString(KEY_DEVICE_PRESET_ID, value).apply()

    var showOnLockScreen: Boolean
        get() = prefs.getBoolean(KEY_SHOW_ON_LOCK_SCREEN, false)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_ON_LOCK_SCREEN, value).apply()

    var batteryOptPromptDismissed: Boolean
        get() = prefs.getBoolean(KEY_BATTERY_OPT_PROMPT_DISMISSED, false)
        set(value) = prefs.edit().putBoolean(KEY_BATTERY_OPT_PROMPT_DISMISSED, value).apply()

    // NEW: when true, the overlay icon is tinted based on battery level
    // (green -> amber -> red) without changing the icon's shape.
    var isBatteryColorReactive: Boolean
        get() = prefs.getBoolean(KEY_BATTERY_COLOR_REACTIVE, false)
        set(value) = prefs.edit().putBoolean(KEY_BATTERY_COLOR_REACTIVE, value).apply()
}