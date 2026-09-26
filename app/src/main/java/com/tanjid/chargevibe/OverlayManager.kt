package com.tanjid.chargevibe

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.tanjid.chargevibe.model.CuteIconRegistry
import com.tanjid.chargevibe.model.DesignType
import com.tanjid.chargevibe.model.DevicePresets
import java.io.File

class OverlayManager(private val context: Context) {

    companion object {
        private const val TAG = "ChargeVibe_Overlay"
        private const val CUTE_ID_OFFSET = 1000
    }

    private var windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var overlayView: View? = null
    private var animatorSet: AnimatorSet? = null
    private val settings = SettingsManager(context)

    // NEW: charging state, set externally by OverlayService when it detects
    // ACTION_POWER_CONNECTED / ACTION_POWER_DISCONNECTED. Defaults to false.
    // If your service never calls setChargingState(), the animation logic
    // just falls back to battery-level-only behavior — nothing breaks.
    private var isCharging: Boolean = false

    fun getSystemStatusBarHeight(): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            (28 * context.resources.displayMetrics.density).toInt()
        }
    }

    fun snapToSystemBattery() {
        val density = context.resources.displayMetrics.density
        val screenWidth = getScreenWidth()
        val widthPx = (settings.overlayWidthDp * density).toInt()
        val heightPx = (settings.overlayHeightDp * density).toInt()
        val barHeight = getSystemStatusBarHeight()

        settings.posX = screenWidth - widthPx - (4 * density).toInt()
        settings.posY = ((barHeight - heightPx) / 2).coerceAtLeast(0)
    }

    private fun getScreenWidth(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.width()
        } else {
            @Suppress("DEPRECATION")
            val rect = android.graphics.Rect()
            windowManager.defaultDisplay.getRectSize(rect)
            rect.width()
        }
    }

    private fun computeFinalPosition(): Pair<Int, Int> {
        val density = context.resources.displayMetrics.density
        val preset = DevicePresets.byId(settings.devicePresetId)

        val baseX = settings.posX
        val baseY = settings.posY

        val offsetXPx = (preset.offsetXDp * density).toInt()
        val offsetYPx = (preset.offsetYDp * density).toInt()

        return Pair(baseX + offsetXPx, baseY + offsetYPx)
    }

    private fun buildLayoutParams(): WindowManager.LayoutParams {
        val density = context.resources.displayMetrics.density
        val widthPx = (settings.overlayWidthDp * density).toInt().coerceAtLeast(20)
        val heightPx = (settings.overlayHeightDp * density).toInt().coerceAtLeast(10)

        if (settings.posX == -9999) {
            snapToSystemBattery()
        }

        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        var flags = if (settings.isDragMode) {
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        } else {
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }

        if (settings.showOnLockScreen) {
            flags = flags or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        }

        val (finalX, finalY) = computeFinalPosition()

        return WindowManager.LayoutParams(
            widthPx,
            heightPx,
            layoutFlag,
            flags,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = finalX
            y = finalY

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun showOverlay(batteryLevel: Int) {
        if (overlayView != null) {
            updateOverlay(batteryLevel)
            return
        }

        val inflater = LayoutInflater.from(context)
        overlayView = inflater.inflate(R.layout.overlay_charging, null)

        val params = buildLayoutParams()
        val density = context.resources.displayMetrics.density
        val heightPx = (settings.overlayHeightDp * density).toInt().coerceAtLeast(10)

        setupDesign(batteryLevel, heightPx)
        applyMask()

        var initX = 0
        var initY = 0
        var touchX = 0f
        var touchY = 0f

        overlayView?.setOnTouchListener { _, event ->
            if (!settings.isDragMode) return@setOnTouchListener false
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initX = params.x
                    initY = params.y
                    touchX = event.rawX
                    touchY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val d = context.resources.displayMetrics.density
                    val preset = DevicePresets.byId(settings.devicePresetId)
                    val offsetXPx = (preset.offsetXDp * d).toInt()
                    val offsetYPx = (preset.offsetYDp * d).toInt()

                    val newFinalX = initX + (event.rawX - touchX).toInt()
                    val newFinalY = initY + (event.rawY - touchY).toInt()

                    settings.posX = newFinalX - offsetXPx
                    settings.posY = newFinalY - offsetYPx

                    params.x = newFinalX
                    params.y = newFinalY
                    try {
                        windowManager.updateViewLayout(overlayView, params)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    true
                }
                else -> false
            }
        }

        try {
            windowManager.addView(overlayView, params)
            startAnimation(batteryLevel)
            Log.d(TAG, "showOverlay: added successfully at (${params.x}, ${params.y})")
        } catch (e: Exception) {
            Log.e(TAG, "showOverlay error", e)
            overlayView = null
        }
    }

    fun updateOverlay(batteryLevel: Int) {
        hideOverlay()
        showOverlay(batteryLevel)
    }

    /**
     * NEW: External charging-state setter. Call this from OverlayService when
     * you receive ACTION_POWER_CONNECTED / ACTION_POWER_DISCONNECTED so the
     * animation can react. Also triggers a re-animation immediately.
     *
     * Safe no-op design: if you never call this, isCharging stays false and
     * animations fall back to battery-level-based behavior. Nothing breaks.
     */
    fun setChargingState(charging: Boolean) {
        if (this.isCharging == charging) return
        this.isCharging = charging
        // Re-run animation with the new charging context (if overlay is visible)
        overlayView?.let {
            // Read the last level from settings-driven design; we don't have
            // the raw battery number here, so caller should also updateOverlay()
            // — but re-running startAnimation with a neutral level still works
            // for the charging-priority modes below.
            startAnimation(lastKnownBatteryLevel)
        }
    }

    // Cached so setChargingState() can re-trigger animation without needing
    // caller to pass the level again.
    private var lastKnownBatteryLevel: Int = 100

    private fun applyMask() {
        val root = overlayView?.findViewById<View>(R.id.overlayRoot) ?: return
        val dragBorder = overlayView?.findViewById<View>(R.id.dragBorder)
        dragBorder?.visibility = if (settings.isDragMode) View.VISIBLE else View.GONE

        root.setBackgroundColor(settings.maskColor)
        root.alpha = settings.transparency
    }

    private fun setupDesign(batteryLevel: Int, heightPx: Int) {
        val iconView = overlayView?.findViewById<ImageView>(R.id.overlayIcon) ?: return
        val emojiView = overlayView?.findViewById<TextView>(R.id.overlayEmoji) ?: return
        val customImageView = overlayView?.findViewById<ImageView>(R.id.overlayCustomImage) ?: return

        iconView.visibility = View.GONE
        emojiView.visibility = View.GONE
        customImageView.visibility = View.GONE

        val healthTint = getHealthTint(batteryLevel)

        when (settings.designType) {
            DesignType.BUILTIN_DRAWABLE -> {
                iconView.visibility = View.VISIBLE
                val drawableRes = getDrawableForBatteryLevel(batteryLevel)

                if (drawableRes >= CUTE_ID_OFFSET) {
                    val cuteId = drawableRes - CUTE_ID_OFFSET
                    val bitmapSize = heightPx.coerceAtLeast(48)
                    try {
                        val drawable = CuteIconRegistry.generateDrawable(context, cuteId, bitmapSize)
                        iconView.setImageDrawable(drawable)
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to generate cute icon $cuteId", e)
                        iconView.setImageResource(R.drawable.ic_bolt)
                    }
                } else {
                    iconView.setImageResource(drawableRes)
                }

                if (healthTint != null) {
                    iconView.setColorFilter(healthTint, PorterDuff.Mode.SRC_IN)
                } else {
                    iconView.clearColorFilter()
                }
            }
            DesignType.EMOJI -> {
                emojiView.visibility = View.VISIBLE
                emojiView.text = getEmojiForBatteryLevel(batteryLevel)
                emojiView.setTextSize(TypedValue.COMPLEX_UNIT_PX, heightPx * 0.75f)
            }
            DesignType.CUSTOM_IMAGE -> {
                val path = settings.customImagePath
                if (path != null && File(path).exists()) {
                    customImageView.visibility = View.VISIBLE
                    val bitmap = BitmapFactory.decodeFile(path)
                    customImageView.setImageBitmap(bitmap)
                    customImageView.clearColorFilter()
                } else {
                    iconView.visibility = View.VISIBLE
                    iconView.setImageResource(R.drawable.ic_bolt)
                    iconView.clearColorFilter()
                }
            }
        }
    }

    private fun getDrawableForBatteryLevel(level: Int): Int {
        return settings.drawableRes
    }

    private fun getEmojiForBatteryLevel(level: Int): String {
        return settings.emoji
    }

    private fun getHealthTint(level: Int): Int? {
        if (!settings.isBatteryColorReactive) return null
        return when {
            level >= 80 -> Color.parseColor("#4CAF50")
            level >= 50 -> Color.parseColor("#8BC34A")
            level >= 20 -> Color.parseColor("#FFC107")
            else -> Color.parseColor("#F44336")
        }
    }

    // =========================================================================
    // NEW: rich reactive animations. Existing pulse + blink from your old code
    // are preserved; charging state + health tier now select from 5 modes.
    // =========================================================================
    //
    // Mode priority (highest wins):
    //   1. Charging → bounce (Y translation) + glow (alpha breath)
    //   2. Level < 20  → shake (X translation) + fast blink
    //   3. Level 20-49 → pulse (existing) + tiny wobble (rotation)
    //   4. Level 50-79 → pulse (existing, unchanged)
    //   5. Level >= 80 → calm breathing (slow, subtle scale)
    //
    // All modes respect settings.enableAnimation (off = static).
    // All modes respect settings.animSpeed as base duration where sensible.
    // =========================================================================
    private fun startAnimation(batteryLevel: Int) {
        animatorSet?.cancel()
        val view = overlayView ?: return

        // Cache so setChargingState() can re-fire without a level.
        lastKnownBatteryLevel = batteryLevel

        // Always reset transform so switching modes doesn't leave stale values.
        view.scaleX = 1f
        view.scaleY = 1f
        view.translationX = 0f
        view.translationY = 0f
        view.rotation = 0f
        view.alpha = settings.transparency

        if (!settings.enableAnimation) {
            return
        }

        val baseDuration = settings.animSpeed.toLong()

        val animators = mutableListOf<android.animation.Animator>()

        when {
            // -----------------------------------------------------------------
            // MODE 1: CHARGING — happy bouncy overlay + glowing alpha
            // -----------------------------------------------------------------
            isCharging -> {
                val bounce = ObjectAnimator.ofFloat(view, "translationY", 0f, -8f, 0f).apply {
                    duration = (baseDuration * 0.75f).toLong().coerceAtLeast(400L)
                    repeatCount = ValueAnimator.INFINITE
                    interpolator = OvershootInterpolator(2f)
                }
                val glow = ObjectAnimator.ofFloat(
                    view, "alpha",
                    settings.transparency, (settings.transparency * 0.6f).coerceAtLeast(0.3f), settings.transparency
                ).apply {
                    duration = (baseDuration * 0.9f).toLong().coerceAtLeast(500L)
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                val liftScale = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.08f, 1f).apply {
                    duration = (baseDuration * 0.75f).toLong().coerceAtLeast(400L)
                    repeatCount = ValueAnimator.INFINITE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                val liftScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.08f, 1f).apply {
                    duration = (baseDuration * 0.75f).toLong().coerceAtLeast(400L)
                    repeatCount = ValueAnimator.INFINITE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                animators.addAll(listOf(bounce, glow, liftScale, liftScaleY))
            }

            // -----------------------------------------------------------------
            // MODE 2: CRITICAL (<20%) — shake + fast blink
            // -----------------------------------------------------------------
            batteryLevel in 1..20 -> {
                val shake = ObjectAnimator.ofFloat(view, "translationX", 0f, -4f, 4f, -3f, 3f, 0f).apply {
                    duration = 500L
                    repeatCount = ValueAnimator.INFINITE
                    interpolator = LinearInterpolator()
                }
                val blink = ObjectAnimator.ofFloat(
                    view, "alpha",
                    settings.transparency, 0.25f, settings.transparency
                ).apply {
                    duration = 500L
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                val pulseX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.15f, 1f).apply {
                    duration = baseDuration
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                val pulseY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.15f, 1f).apply {
                    duration = baseDuration
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                animators.addAll(listOf(shake, blink, pulseX, pulseY))
            }

            // -----------------------------------------------------------------
            // MODE 3: LOW (20-49%) — pulse + tiny rotation wobble
            // -----------------------------------------------------------------
            batteryLevel in 20..49 -> {
                val pulseX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.15f, 1f).apply {
                    duration = (baseDuration * 0.85f).toLong().coerceAtLeast(500L)
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                val pulseY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.15f, 1f).apply {
                    duration = (baseDuration * 0.85f).toLong().coerceAtLeast(500L)
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                val wobble = ObjectAnimator.ofFloat(view, "rotation", -4f, 4f, -4f).apply {
                    duration = (baseDuration * 1.5f).toLong().coerceAtLeast(1000L)
                    repeatCount = ValueAnimator.INFINITE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                animators.addAll(listOf(pulseX, pulseY, wobble))
            }

            // -----------------------------------------------------------------
            // MODE 4: NORMAL (50-79%) — your existing pulse, unchanged
            // -----------------------------------------------------------------
            batteryLevel in 50..79 -> {
                val pulseX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.15f, 1f).apply {
                    duration = baseDuration
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                val pulseY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.15f, 1f).apply {
                    duration = baseDuration
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                animators.addAll(listOf(pulseX, pulseY))
            }

            // -----------------------------------------------------------------
            // MODE 5: HEALTHY (>=80%) — calm slow breathing
            // -----------------------------------------------------------------
            else -> {
                val breatheX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.06f, 1f).apply {
                    duration = (baseDuration * 1.6f).toLong().coerceAtLeast(1200L)
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                val breatheY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.06f, 1f).apply {
                    duration = (baseDuration * 1.6f).toLong().coerceAtLeast(1200L)
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    interpolator = AccelerateDecelerateInterpolator()
                }
                animators.addAll(listOf(breatheX, breatheY))
            }
        }

        animatorSet = AnimatorSet().apply {
            playTogether(animators)
        }
        animatorSet?.start()
    }

    fun hideOverlay() {
        animatorSet?.cancel()
        animatorSet = null
        overlayView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        overlayView = null
    }

    fun isShowing(): Boolean = overlayView != null
}