package com.tanjid.chargevibe

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.tanjid.chargevibe.adapter.DesignAdapter
import com.tanjid.chargevibe.icons.CuteIconGenerator
import com.tanjid.chargevibe.model.ChargingDesign
import com.tanjid.chargevibe.model.DesignType
import com.tanjid.chargevibe.model.DevicePresets
import com.tanjid.chargevibe.ui.IconDrawerSheet
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    companion object {
        // Cute icons use pseudo-drawable IDs starting from CUTE_ID_OFFSET
        private const val CUTE_ID_OFFSET = 1000
    }

    private lateinit var settingsManager: SettingsManager
    private lateinit var designAdapter: DesignAdapter
    private val designs = mutableListOf<ChargingDesign>()

    // Views
    private lateinit var btnOverlayPerm: MaterialButton
    private lateinit var btnNotifPerm: MaterialButton
    private lateinit var btnBatteryPerm: MaterialButton
    private lateinit var btnActivate: MaterialButton
    private lateinit var btnDeactivate: MaterialButton
    private lateinit var btnSnapToBattery: MaterialButton
    private lateinit var btnToggleDrag: MaterialButton
    private lateinit var btnShowLimitationGuide: MaterialButton
    private lateinit var btnNudgeUp: MaterialButton
    private lateinit var btnNudgeDown: MaterialButton
    private lateinit var btnNudgeLeft: MaterialButton
    private lateinit var btnNudgeRight: MaterialButton
    private lateinit var btnUploadImage: MaterialButton
    private lateinit var btnSelectEmoji: MaterialButton
    private lateinit var btnColorBlack: MaterialButton
    private lateinit var btnColorDark: MaterialButton
    private lateinit var btnColorTransparent: MaterialButton
    private lateinit var btnBrowseAllIcons: MaterialButton   // NEW
    private lateinit var switchAlwaysShow: SwitchMaterial
    private lateinit var switchAnimation: SwitchMaterial
    private lateinit var switchLockScreen: SwitchMaterial
    private lateinit var switchBatteryColors: SwitchMaterial
    private lateinit var spinnerDevicePreset: Spinner
    private lateinit var designRecyclerView: RecyclerView
    private lateinit var sliderHeight: Slider
    private lateinit var sliderWidth: Slider
    private lateinit var sliderTransparency: Slider
    private lateinit var sliderAnimSpeed: Slider
    private lateinit var statusText: TextView
    private lateinit var batteryText: TextView
    private lateinit var statusDot: android.view.View

    private val overlayPermLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { updatePermissionButtons() }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri -> saveCustomImage(uri) }
        }
    }

    private val notifPermLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { updatePermissionButtons() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingsManager = SettingsManager(this)
        initViews()
        setupDesigns()
        setupDevicePresetSpinner()
        setupListeners()
        updatePermissionButtons()
        updateServiceStatus()
        loadSavedSettings()
        updateBatteryInfo()
    }

    override fun onResume() {
        super.onResume()
        updatePermissionButtons()
        updateServiceStatus()
        updateBatteryInfo()
    }

    private fun initViews() {
        btnOverlayPerm = findViewById(R.id.btnOverlayPerm)
        btnNotifPerm = findViewById(R.id.btnNotifPerm)
        btnBatteryPerm = findViewById(R.id.btnBatteryPerm)
        btnActivate = findViewById(R.id.btnActivate)
        btnDeactivate = findViewById(R.id.btnDeactivate)
        btnSnapToBattery = findViewById(R.id.btnSnapToBattery)
        btnToggleDrag = findViewById(R.id.btnToggleDrag)
        btnShowLimitationGuide = findViewById(R.id.btnShowLimitationGuide)
        btnNudgeUp = findViewById(R.id.btnNudgeUp)
        btnNudgeDown = findViewById(R.id.btnNudgeDown)
        btnNudgeLeft = findViewById(R.id.btnNudgeLeft)
        btnNudgeRight = findViewById(R.id.btnNudgeRight)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        btnSelectEmoji = findViewById(R.id.btnSelectEmoji)
        btnColorBlack = findViewById(R.id.btnColorBlack)
        btnColorDark = findViewById(R.id.btnColorDark)
        btnColorTransparent = findViewById(R.id.btnColorTransparent)
        btnBrowseAllIcons = findViewById(R.id.btnBrowseAllIcons)   // NEW
        switchAlwaysShow = findViewById(R.id.switchAlwaysShow)
        switchAnimation = findViewById(R.id.switchAnimation)
        switchLockScreen = findViewById(R.id.switchLockScreen)
        switchBatteryColors = findViewById(R.id.switchBatteryColors)
        spinnerDevicePreset = findViewById(R.id.spinnerDevicePreset)
        designRecyclerView = findViewById(R.id.designRecyclerView)
        sliderHeight = findViewById(R.id.sliderHeight)
        sliderWidth = findViewById(R.id.sliderWidth)
        sliderTransparency = findViewById(R.id.sliderTransparency)
        sliderAnimSpeed = findViewById(R.id.sliderAnimSpeed)
        statusText = findViewById(R.id.statusText)
        batteryText = findViewById(R.id.batteryText)
        statusDot = findViewById(R.id.statusDot)
    }

    private fun setupDevicePresetSpinner() {
        val presetNames = DevicePresets.ALL.map { it.displayName }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, presetNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDevicePreset.adapter = adapter

        // Select saved preset
        val savedId = settingsManager.devicePresetId
        val index = DevicePresets.ALL.indexOfFirst { it.id == savedId }
        if (index >= 0) {
            spinnerDevicePreset.setSelection(index)
        }

        spinnerDevicePreset.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val preset = DevicePresets.ALL[position]
                if (settingsManager.devicePresetId != preset.id) {
                    settingsManager.devicePresetId = preset.id
                    notifyServiceUpdate()
                    Toast.makeText(this@MainActivity, "Preset: ${preset.displayName}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupDesigns() {
        designs.clear()

        // Original built-in XML drawables (IDs 0-6)
        designs.addAll(
            listOf(
                ChargingDesign(0, "Lightning", DesignType.BUILTIN_DRAWABLE, R.drawable.ic_bolt),
                ChargingDesign(1, "Battery", DesignType.BUILTIN_DRAWABLE, R.drawable.ic_battery_full),
                ChargingDesign(2, "Heart", DesignType.BUILTIN_DRAWABLE, R.drawable.ic_heart_charge),
                ChargingDesign(3, "Rocket", DesignType.BUILTIN_DRAWABLE, R.drawable.ic_rocket),
                ChargingDesign(4, "Star", DesignType.BUILTIN_DRAWABLE, R.drawable.ic_star),
                ChargingDesign(5, "Flame", DesignType.BUILTIN_DRAWABLE, R.drawable.ic_flame),
                ChargingDesign(6, "Energy", DesignType.BUILTIN_DRAWABLE, R.drawable.ic_circle_energy)
            )
        )

        // Emoji designs (IDs 7-14)
        designs.addAll(
            listOf(
                ChargingDesign(7, "⚡ Bolt", DesignType.EMOJI, emoji = "⚡"),
                ChargingDesign(8, "🔋 Battery", DesignType.EMOJI, emoji = "🔋"),
                ChargingDesign(9, "💚 Green", DesignType.EMOJI, emoji = "💚"),
                ChargingDesign(10, "🔥 Fire", DesignType.EMOJI, emoji = "🔥"),
                ChargingDesign(11, "🚀 Rocket", DesignType.EMOJI, emoji = "🚀"),
                ChargingDesign(12, "⭐ Star", DesignType.EMOJI, emoji = "⭐"),
                ChargingDesign(13, "💜 Purple", DesignType.EMOJI, emoji = "💜"),
                ChargingDesign(14, "🌈 Rainbow", DesignType.EMOJI, emoji = "🌈")
            )
        )

        // All 60 CUTE GENERATED ICONS (IDs 1001-1060)
        // These use pseudo-drawableRes = CUTE_ID_OFFSET + cuteIcon.id
        // The OverlayManager checks for IDs >= 1000 and generates them via Canvas
        CuteIconGenerator.ALL_ICONS.forEach { cuteIcon ->
            designs.add(
                ChargingDesign(
                    id = CUTE_ID_OFFSET + cuteIcon.id,
                    name = cuteIcon.name,
                    type = DesignType.BUILTIN_DRAWABLE,
                    drawableRes = CUTE_ID_OFFSET + cuteIcon.id
                )
            )
        }

        designAdapter = DesignAdapter(designs) { design ->
            applySelectedDesign(design)
        }

        designRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        designRecyclerView.adapter = designAdapter
        designAdapter.setSelectedById(settingsManager.selectedDesignId)
    }

    /**
     * Shared design-selection flow used by BOTH the horizontal RecyclerView
     * and the new bottom-sheet drawer. Same behavior in both places — no drift.
     */
    private fun applySelectedDesign(design: ChargingDesign) {
        settingsManager.selectedDesignId = design.id
        settingsManager.designType = design.type
        if (design.type == DesignType.BUILTIN_DRAWABLE && design.drawableRes != null) {
            settingsManager.drawableRes = design.drawableRes
        } else if (design.type == DesignType.EMOJI && design.emoji != null) {
            settingsManager.emoji = design.emoji
        }
        // Keep horizontal picker highlight in sync even if selection came from drawer
        designAdapter.setSelectedById(design.id)
        notifyServiceUpdate()
        Toast.makeText(this, "Design: ${design.name}", Toast.LENGTH_SHORT).show()
    }

    private fun setupListeners() {
        btnOverlayPerm.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                overlayPermLauncher.launch(intent)
            }
        }

        btnNotifPerm.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    notifPermLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }

        btnBatteryPerm.setOnClickListener {
            requestIgnoreBatteryOptimizations()
        }

        btnShowLimitationGuide.setOnClickListener {
            showStatusBarLimitationGuide()
        }

        btnSnapToBattery.setOnClickListener {
            val mgr = OverlayManager(this)
            mgr.snapToSystemBattery()
            notifyServiceUpdate()
            Toast.makeText(this, "Snapped to Status Bar area", Toast.LENGTH_SHORT).show()
        }

        btnToggleDrag.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Grant overlay permission first!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!OverlayService.isRunning) {
                activateService()
                return@setOnClickListener
            }
            val isNowDragging = !settingsManager.isDragMode
            settingsManager.isDragMode = isNowDragging

            if (isNowDragging) {
                btnToggleDrag.text = "🔒 Lock Position"
                btnToggleDrag.backgroundTintList = ContextCompat.getColorStateList(this, R.color.success)
                Toast.makeText(this, "Now drag the overlay on screen!", Toast.LENGTH_LONG).show()
            } else {
                btnToggleDrag.text = "👆 Enable Drag Mode"
                btnToggleDrag.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary)
                Toast.makeText(this, "Locked ✓", Toast.LENGTH_SHORT).show()
            }
            notifyServiceUpdate()
        }

        val nudgePx = (4 * resources.displayMetrics.density).toInt().coerceAtLeast(1)
        btnNudgeUp.setOnClickListener { settingsManager.posY -= nudgePx; notifyServiceUpdate() }
        btnNudgeDown.setOnClickListener { settingsManager.posY += nudgePx; notifyServiceUpdate() }
        btnNudgeLeft.setOnClickListener { settingsManager.posX -= nudgePx; notifyServiceUpdate() }
        btnNudgeRight.setOnClickListener { settingsManager.posX += nudgePx; notifyServiceUpdate() }

        btnColorBlack.setOnClickListener {
            settingsManager.maskColor = Color.BLACK
            notifyServiceUpdate()
            Toast.makeText(this, "Mask: Solid Black", Toast.LENGTH_SHORT).show()
        }
        btnColorDark.setOnClickListener {
            settingsManager.maskColor = Color.parseColor("#1A1A2E")
            notifyServiceUpdate()
            Toast.makeText(this, "Mask: Dark Blue", Toast.LENGTH_SHORT).show()
        }
        btnColorTransparent.setOnClickListener {
            settingsManager.maskColor = Color.TRANSPARENT
            notifyServiceUpdate()
            Toast.makeText(this, "Mask: Clear", Toast.LENGTH_SHORT).show()
        }

        switchAlwaysShow.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.alwaysShow = isChecked
            notifyServiceUpdate()
        }

        switchAnimation.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.enableAnimation = isChecked
            notifyServiceUpdate()
        }

        switchLockScreen.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.showOnLockScreen = isChecked
            notifyServiceUpdate()
        }

        // Battery-health color reactive toggle
        switchBatteryColors.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.isBatteryColorReactive = isChecked
            notifyServiceUpdate()
            val msg = if (isChecked) "Icon will now tint by battery health" else "Battery color tint off"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // NEW: Full-screen icon drawer trigger
        btnBrowseAllIcons.setOnClickListener {
            val drawer = IconDrawerSheet(designs) { selectedDesign ->
                applySelectedDesign(selectedDesign)
            }
            drawer.show(supportFragmentManager, "icon_drawer")
        }

        btnActivate.setOnClickListener { activateService() }
        btnDeactivate.setOnClickListener { deactivateService() }
        btnUploadImage.setOnClickListener { openImagePicker() }
        btnSelectEmoji.setOnClickListener { showEmojiPicker() }

        sliderWidth.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                settingsManager.overlayWidthDp = value
                notifyServiceUpdate()
            }
        }

        sliderHeight.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                settingsManager.overlayHeightDp = value
                notifyServiceUpdate()
            }
        }

        sliderTransparency.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                settingsManager.transparency = value
                notifyServiceUpdate()
            }
        }

        sliderAnimSpeed.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                settingsManager.animSpeed = value
                notifyServiceUpdate()
            }
        }
    }

    private fun loadSavedSettings() {
        try {
            sliderWidth.value = settingsManager.overlayWidthDp.coerceIn(sliderWidth.valueFrom, sliderWidth.valueTo)
        } catch (e: Exception) { sliderWidth.value = 60f }

        try {
            sliderHeight.value = settingsManager.overlayHeightDp.coerceIn(sliderHeight.valueFrom, sliderHeight.valueTo)
        } catch (e: Exception) { sliderHeight.value = 30f }

        try {
            sliderTransparency.value = settingsManager.transparency.coerceIn(sliderTransparency.valueFrom, sliderTransparency.valueTo)
        } catch (e: Exception) { sliderTransparency.value = 1f }

        try {
            sliderAnimSpeed.value = settingsManager.animSpeed.coerceIn(sliderAnimSpeed.valueFrom, sliderAnimSpeed.valueTo)
        } catch (e: Exception) { sliderAnimSpeed.value = 1500f }

        switchAlwaysShow.isChecked = settingsManager.alwaysShow
        switchAnimation.isChecked = settingsManager.enableAnimation
        switchLockScreen.isChecked = settingsManager.showOnLockScreen
        switchBatteryColors.isChecked = settingsManager.isBatteryColorReactive
    }

    private fun updatePermissionButtons() {
        // Overlay
        if (Settings.canDrawOverlays(this)) {
            btnOverlayPerm.text = getString(R.string.granted)
            btnOverlayPerm.isEnabled = false
            btnOverlayPerm.alpha = 0.6f
        } else {
            btnOverlayPerm.text = getString(R.string.grant_permission)
            btnOverlayPerm.isEnabled = true
            btnOverlayPerm.alpha = 1f
        }

        // Notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                btnNotifPerm.text = getString(R.string.granted)
                btnNotifPerm.isEnabled = false
                btnNotifPerm.alpha = 0.6f
            } else {
                btnNotifPerm.text = getString(R.string.grant_permission)
                btnNotifPerm.isEnabled = true
                btnNotifPerm.alpha = 1f
            }
        } else {
            btnNotifPerm.text = getString(R.string.granted)
            btnNotifPerm.isEnabled = false
            btnNotifPerm.alpha = 0.6f
        }

        // Battery Optimization
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        if (pm.isIgnoringBatteryOptimizations(packageName)) {
            btnBatteryPerm.text = getString(R.string.granted)
            btnBatteryPerm.isEnabled = false
            btnBatteryPerm.alpha = 0.6f
        } else {
            btnBatteryPerm.text = getString(R.string.grant_permission)
            btnBatteryPerm.isEnabled = true
            btnBatteryPerm.alpha = 1f
        }
    }

    private fun updateServiceStatus() {
        if (OverlayService.isRunning) {
            statusText.text = "Service Active"
            statusDot.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.success)
            btnActivate.text = "⚡  Active"
            btnActivate.isEnabled = false
            btnActivate.alpha = 0.7f
            btnDeactivate.visibility = android.view.View.VISIBLE
        } else {
            statusText.text = "Service Inactive"
            statusDot.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.danger)
            btnActivate.text = "⚡  Activate ChargeVibe"
            btnActivate.isEnabled = true
            btnActivate.alpha = 1f
            btnDeactivate.visibility = android.view.View.GONE
        }
    }

    private fun updateBatteryInfo() {
        val bm = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val charging = bm.isCharging
        batteryText.text = "🔋 $level%" + if (charging) " ⚡" else ""
    }

    private fun activateService() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Grant overlay permission first", Toast.LENGTH_LONG).show()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Grant notification permission first", Toast.LENGTH_LONG).show()
                return
            }
        }

        settingsManager.isServiceActive = true
        val intent = Intent(this, OverlayService::class.java)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        android.os.Handler(mainLooper).postDelayed({
            updateServiceStatus()
        }, 500)

        Toast.makeText(this, "ChargeVibe activated ⚡", Toast.LENGTH_SHORT).show()

        // If battery opt not yet allowed, prompt after activation
        promptBatteryOptimizationIfNeeded()
    }

    private fun deactivateService() {
        settingsManager.isServiceActive = false
        settingsManager.isDragMode = false
        btnToggleDrag.text = "👆 Enable Drag Mode"
        btnToggleDrag.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary)

        val intent = Intent(this, OverlayService::class.java).apply {
            action = OverlayService.ACTION_STOP
        }
        startService(intent)

        android.os.Handler(mainLooper).postDelayed({
            updateServiceStatus()
        }, 500)

        Toast.makeText(this, "Deactivated", Toast.LENGTH_SHORT).show()
    }

    private fun notifyServiceUpdate() {
        if (OverlayService.isRunning) {
            val intent = Intent(this, OverlayService::class.java).apply {
                action = OverlayService.ACTION_UPDATE
            }
            try {
                startService(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // === Battery Optimization ===
    private fun requestIgnoreBatteryOptimizations() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        val isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName)
        if (isIgnoring) {
            Toast.makeText(this, "Already granted ✓", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            @SuppressLint("BatteryLife")
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        } catch (e: Exception) {
            openManufacturerBatterySettingsGuide()
        }
    }

    private fun promptBatteryOptimizationIfNeeded() {
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName) &&
            !settingsManager.batteryOptPromptDismissed
        ) {
            AlertDialog.Builder(this)
                .setTitle("Prevent Android from stopping ChargeVibe")
                .setMessage(
                    "Android may kill this app in the background to save battery. " +
                            "Grant unrestricted background permission so the overlay stays alive."
                )
                .setPositiveButton("Fix Now") { _, _ -> requestIgnoreBatteryOptimizations() }
                .setNegativeButton("Later", null)
                .setNeutralButton("Don't ask again") { _, _ ->
                    settingsManager.batteryOptPromptDismissed = true
                }
                .show()
        }
    }

    // === "How to Hide System Battery" Guide ===
    private fun showStatusBarLimitationGuide() {
        val manufacturer = Build.MANUFACTURER.lowercase()

        val (title, steps) = when {
            manufacturer.contains("samsung") -> "Samsung — Good Lock" to
                    "1. Install 'Good Lock' from Galaxy Store\n" +
                    "2. Open the 'QuickStar' module\n" +
                    "3. Go to Status Bar > Icons\n" +
                    "4. Hide the battery icon/percentage"
            manufacturer.contains("xiaomi") || manufacturer.contains("redmi") || manufacturer.contains("poco") -> "Xiaomi / MIUI / HyperOS" to
                    "1. Settings > Notifications & Control Center > Status bar\n" +
                    "2. Toggle off 'Show battery percentage' / battery icon\n" +
                    "(Exact path varies by MIUI/HyperOS version)"
            manufacturer.contains("oneplus") -> "OnePlus — Customization" to
                    "1. Settings > Customization > Status bar\n" +
                    "2. Adjust battery style / percentage display"
            manufacturer.contains("oppo") -> "OPPO — ColorOS" to
                    "1. Settings > Notifications & status bar > Status bar\n" +
                    "2. Battery style > Hidden"
            manufacturer.contains("vivo") -> "Vivo — FunTouch / OriginOS" to
                    "1. Settings > Notifications & status bar > Status bar\n" +
                    "2. Battery percentage / icon > Hidden"
            manufacturer.contains("google") -> "Google Pixel" to
                    "Stock Android does NOT let you hide the battery icon. " +
                    "You'll need to enable Developer Options > SystemUI Tuner (if available) " +
                    "or use root tools (Magisk + module) to hide it."
            else -> "Your device" to
                    "Most Android skins only let you toggle the battery " +
                    "PERCENTAGE, not hide the icon entirely, unless you use " +
                    "root tools (Magisk + a SystemUI module) or a custom ROM. " +
                    "ChargeVibe cannot do this itself — no app on the Play " +
                    "Store can, by Android OS design."
        }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(
                "ChargeVibe's overlay sits below the system status bar and " +
                        "can never draw over or replace it — this is an Android " +
                        "security restriction, not a bug in this app.\n\n$steps"
            )
            .setPositiveButton("Got it", null)
            .show()
    }

    private fun openManufacturerBatterySettingsGuide() {
        AlertDialog.Builder(this)
            .setTitle("Allow ChargeVibe to run in the background")
            .setMessage(
                "Your device manufacturer aggressively kills background " +
                        "apps. Go to:\nSettings > Battery > App battery usage > " +
                        "ChargeVibe > set to 'Unrestricted' / 'No restrictions', " +
                        "and disable any 'Auto-launch management' toggle for this app."
            )
            .setPositiveButton("Open Settings") { _, _ ->
                try {
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:$packageName")
                    })
                } catch (_: Exception) { /* no-op */ }
            }
            .setNegativeButton("Later", null)
            .show()
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        imagePickerLauncher.launch(intent)
    }

    private fun saveCustomImage(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val maxSize = 256
            val scaledBitmap = if (bitmap.width > maxSize || bitmap.height > maxSize) {
                val ratio = minOf(
                    maxSize.toFloat() / bitmap.width,
                    maxSize.toFloat() / bitmap.height
                )
                Bitmap.createScaledBitmap(
                    bitmap,
                    (bitmap.width * ratio).toInt(),
                    (bitmap.height * ratio).toInt(),
                    true
                )
            } else bitmap

            val dir = File(filesDir, "custom_icons")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "custom_${System.currentTimeMillis()}.png")
            val fos = FileOutputStream(file)
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()

            settingsManager.customImagePath = file.absolutePath
            settingsManager.designType = DesignType.CUSTOM_IMAGE
            notifyServiceUpdate()
            Toast.makeText(this, "Custom image set 🎨", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEmojiPicker() {
        val emojis = listOf(
            "😀", "😃", "😄", "😁", "😆", "😅", "🤣", "😂", "🙂", "🙃", "😉", "😊", "😇", "🥰", "😍", "🤩",
            "😘", "😗", "😚", "😙", "😋", "😛", "😜", "🤪", "😝", "🤑", "🤗", "🤭", "🤫", "🤔", "🤐", "🤨",
            "😐", "😑", "😶", "😏", "😒", "🙄", "😬", "🤥", "😌", "😔", "😪", "🤤", "😴", "😷", "🤒", "🤕",
            "🤢", "🤮", "🤧", "🥵", "🥶", "🥴", "😵", "🤯", "🤠", "🥳", "😎", "🤓", "🧐", "😕", "😟", "🙁",
            "😮", "😯", "😲", "😳", "🥺", "😦", "😧", "📁", "😰", "😱", "🥱", "😤", "😡", "🤬", "😈", "👿",
            "💀", "☠️", "💩", "🤡", "👹", "👺", "👻", "👽", "👾", "🤖",

            // Gestures & Body
            "👋", "🤚", "🖐️", "✋", "🖖", "👌", "🤌", "🤏", "✌️", "🤞", "🤟", "🤘", "🤙", "👈", "👉", "👆",
            "🖕", "👇", "☝️", "👍", "👎", "✊", "👊", "🤛", "🤜", "👏", "🙌", "👐", "🤲", "🤝", "🙏", "✍️",
            "💅", "🤳", "💪", "👂", "👃", "🧠", "🦷", "🦴", "👀", "👁️", "👅", "👄",

            // Hearts & Expressions
            "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔", "❣️", "💕", "💞", "💓", "💗", "💖",
            "💘", "💝", "💟", "🔥", "✨", "🌟", "💫", "💥", "💢", "💦", "💧", "💨", "🕳️", "💬", "💭",

            // Animals & Nature
            "🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐨", "🐯", "🦁", "🐮", "🐷", "🐸", "🐵", "🙈",
            "🙉", "🙊", "🐔", "🐧", "🐦", "🐤", "🐣", "🐥", "🦆", "🦅", "🦉", "🦇", "🐺", "🐗", "🐴", "🦄",
            "🐝", "🐛", "🦋", "🐌", "🐞", "🐜", "🦟", "🦗", "🕷️", "🦂", "🐢", "🐍", "🦎", "🐙", "🦑", "🦐",
            "🦞", "🦀", "🐡", "🐠", "🐟", "🐬", "🐳", "🐋", "🦈", "🐊", "🐅", "🐆", "zebra", "🦍", "🦧", "🐘",
            "🦛", "🦏", "🐪", "🦒", "🦘", "🦥", "🦦", "🦨", "🦡", "🦔", "🐾", "🦩", "🦚", "🦜", "🕊️", "🌲",
            "🌳", "🌴", "🌵", "🌾", "🌿", "☘️", "🍀", "🍁", "🍂", "🍃", "🍄", "🐚", "💐", "🌸", "💮", "🌹",
            "🥀", "🌺", "🌻", "🌼", "🌷", "⚡", "🌈", "🌊", "❄️", "🔥",

            // Food & Drink
            "🍎", "🍏", "🍐", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓", "🫐", "🍈", "🍒", "🍑", "🥭", "🍍", "🥥",
            "🥝", "🍅", "🥑", "🥦", "🥬", "🥒", "🌶️", "🌽", "🥕", "🧄", "🧅", "🥔", "🥐", "🥯", "🍞", "🥖",
            "🥨", "🧀", "🥚", "🍳", "🥞", "🧇", "🥓", "🥩", "🍗", "🍖", "🌭", "🍔", "🍟", "🍕", "🥪", "🥙",
            "🌮", "🌯", "🥗", "🥘", "🥫", "🍝", "🍜", "🍲", "🍛", "🍣", "🍱", "🥟", "🦪", "🍤", "🍙", "🍚",
            "🍦", "🍧", "🍨", "🍩", "🍪", "🎂", "🍰", "🧁", "🥧", "🍫", "🍬", "🍭", "🍮", "🍯", "🍼", "🥛",
            "☕", "🫖", "🍵", "🧃", "🥤", "🧋", "🍶", "🍺", "🍻", "🥂", "🍷", "🥃", "🍸", "🍹", "🍾", "🧊",

            // Activities & Sports
            "⚽", "🏀", "🏈", "⚾", "🥎", "🎾", "🏐", "🏉", "🥏", "🎱", "🪀", "🏓", "🏸", "🏒", "🏑", "🥍",
            "🏏", "🪃", "🥅", "⛳", "🏹", "🎣", "🤿", "🥊", "🥋", "🎽", "🛹", "🛼", "🛷", "⛸️", "🥌", "🎿",
            "🏆", "🥇", "🥈", "🥉", "🏅", "🎖️", "🎫", "🎟️", "🎪", "🤹", "🎭", "🎨", "🎬", "🎤", "🎧", "🎼",
            "🎹", "🥁", "🎷", "🎺", "🎸", "🪕", "🎻", "🎲", "♟️", "🎯", "🎳", "🎮", "🎰", "🧩",

            // Travel & Objects
            "🚗", "🚕", "🚙", "🚌", "🏎️", "🚓", "🚑", "🚒", "🚐", "🛻", "🚚", "🚛", "🚜", "🛵", "🏍️", "🚲",
            "🛴", "🚨", "🚀", "🛸", "🚁", "🛶", "⛵", "🚤", "🛥️", "🛳️", "⚙️", "⚓", "⛽", "🚧", "🎡", "🎢",
            "🏗️", "🌁", "🗼", "🏭", "🌋", "🏔️", "🏕️", "🏖️", "🏜️", "🏝️", "🏠", "🏢", "🏣", "🏥", "🏦", "🏨",
            "🏪", "🏫", "🏰", "💒", "⛪", "🕌", "🛕", "🕍", "⛩️", "⌚", "📱", "💻", "⌨️", "🖥️", "🖨️", "🖱️",
            "📷", "📸", "📹", "📺", "🔍", "🔎", "🕯️", "💡", "🔦", "🏮", "📔", "📕", "📖", "📗", "📘", "📙",
            "📚", "📓", "📑", "📜", "📄", "📰", "💰", "🪙", "💳", "💎", "⚖️", "🔧", "🔨", "💣", "🛡️", "🔑",

            // Symbols & UI Controls
            "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "✅", "❌", "⚠️", "💯", "📍", "🔒", "🔓",
            "❓", "❗", "⬆️", "⬇️", "⬅️", "➡️", "➕", "➖", "✖️", "➗", "♾️", "💲", "🔣", "🔤", "🅰️", "🅱️",
            "🆎", "🆑", "🅾️", "🆘", "🛑", "⛔", "📛", "🚫", "☢️", "☣️", "⬆️", "↗️", "➡️", "↘️", "⬇️", "↙️",
            "⬅️", "↖️", "↕️", "↔️", "🔄", "◀️", "▶️", "🔽", "🔼", "⏸️", "⏹️", "⏺️", "🔔", "🔕", "🚩", "🏁"
        )

        val dialogView = layoutInflater.inflate(R.layout.dialog_customize, null)
        val grid = dialogView.findViewById<GridLayout>(R.id.emojiGrid)

        val dialog = AlertDialog.Builder(this, R.style.Theme_ChargeVibe)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_card)

        emojis.forEach { emoji ->
            val tv = TextView(this).apply {
                text = emoji
                textSize = 28f
                gravity = Gravity.CENTER
                setPadding(16, 16, 16, 16)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
                setOnClickListener {
                    settingsManager.emoji = emoji
                    settingsManager.designType = DesignType.EMOJI
                    notifyServiceUpdate()
                    dialog.dismiss()
                    Toast.makeText(this@MainActivity, "Emoji: $emoji", Toast.LENGTH_SHORT).show()
                }
            }
            grid.addView(tv)
        }

        dialog.show()
    }
}

// Convenience alias so @SuppressLint compiles
private typealias SuppressLint = android.annotation.SuppressLint