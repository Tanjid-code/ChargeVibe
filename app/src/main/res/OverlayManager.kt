import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.tanjid.chargevibe.model.CuteIconRegistry
import com.tanjid.chargevibe.model.DesignType
import com.tanjid.chargevibe.model.DevicePresets
import java.io.File

class OverlayManager(private val context: Context) {

    companion object {
        private const val TAG = "ChargeVibe_Overlay"
        // Cute icons use pseudo-drawable IDs starting from CUTE_ID_OFFSET
        private const val CUTE_ID_OFFSET = 1000
    }

    private var windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var overlayView: View? = null
    private var animatorSet: AnimatorSet? = null
    private val settings = SettingsManager(context)

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

    /**
     * Compute the final on-screen coordinates by applying the device preset
     * offset on top of the user's saved base X/Y.
     */
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

        // Base flags
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

        // Show on lock screen when enabled
        if (settings.showOnLockScreen) {
            flags = flags or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        }

        // Apply per-device offset preset on top of user X/Y
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
                    // Store the raw base without the preset offset
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

        when (settings.designType) {
            DesignType.BUILTIN_DRAWABLE -> {
                iconView.visibility = View.VISIBLE
                val drawableRes = getDrawableForBatteryLevel(batteryLevel)

                // If this is a cute generated icon (ID >= 1000), draw it programmatically
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
                    // Regular XML drawable
                    iconView.setImageResource(drawableRes)
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
                } else {
                    iconView.visibility = View.VISIBLE
                    iconView.setImageResource(R.drawable.ic_bolt)
                }
            }
        }
    }

    // === User's selection is always respected ===
    // Low-battery / full-charge feedback is conveyed via the blink animation
    // in startAnimation() below — that's the correct place for "reacts to battery level."
    private fun getDrawableForBatteryLevel(level: Int): Int {
        return settings.drawableRes
    }

    private fun getEmojiForBatteryLevel(level: Int): String {
        return settings.emoji
    }

    private fun startAnimation(batteryLevel: Int) {
        animatorSet?.cancel()
        val view = overlayView ?: return

        if (!settings.enableAnimation) {
            view.scaleX = 1f
            view.scaleY = 1f
            return
        }

        val duration = settings.animSpeed.toLong()

        val pulseX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.15f, 1f).apply {
            this.duration = duration
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val pulseY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.15f, 1f).apply {
            this.duration = duration
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        animatorSet = AnimatorSet().apply {
            playTogether(pulseX, pulseY)
        }

        if (batteryLevel in 1..20) {
            val blink = ObjectAnimator.ofFloat(view, "alpha", settings.transparency, 0.3f, settings.transparency).apply {
                this.duration = 600
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
            }
            animatorSet?.playTogether(pulseX, pulseY, blink)
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