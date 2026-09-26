package com.tanjid.chargevibe.model

/**
 * Per-device X/Y offset presets for aligning the overlay closer to where
 * each OEM actually renders the status bar battery icon.
 *
 * IMPORTANT: these dp values are STARTING SCAFFOLDING, not measured truth.
 * Calibrate the real numbers per device using the existing nudge buttons,
 * then feed the corrected values back in here.
 */
data class DevicePreset(
    val id: String,
    val displayName: String,
    val offsetXDp: Int,
    val offsetYDp: Int,
    val statusBarHeightDp: Int
)

object DevicePresets {

    val ALL: List<DevicePreset> = listOf(
        DevicePreset("auto", "Auto / Other (no offset)", 0, 0, 24),
        DevicePreset("pixel_8", "Google Pixel 8 / 8 Pro", -8, 2, 30),
        DevicePreset("pixel_7", "Google Pixel 7 / 7 Pro", -8, 2, 30),
        DevicePreset("samsung_s24", "Samsung Galaxy S24 series", -12, 4, 28),
        DevicePreset("samsung_s23", "Samsung Galaxy S23 series", -12, 4, 28),
        DevicePreset("xiaomi_14", "Xiaomi 14 series", -10, 6, 32),
        DevicePreset("oneplus_12", "OnePlus 12", -10, 4, 30),
        DevicePreset("oppo_generic", "OPPO (ColorOS)", -10, 6, 32),
        DevicePreset("vivo_generic", "Vivo (FunTouch / OriginOS)", -10, 6, 32)
    )

    fun byId(id: String): DevicePreset = ALL.firstOrNull { it.id == id } ?: ALL[0]

    fun guessFromBuild(manufacturer: String, model: String): DevicePreset {
        val m = manufacturer.lowercase()
        return when {
            m.contains("google") -> byId("pixel_8")
            m.contains("samsung") -> byId("samsung_s24")
            m.contains("xiaomi") -> byId("xiaomi_14")
            m.contains("oneplus") -> byId("oneplus_12")
            m.contains("oppo") -> byId("oppo_generic")
            m.contains("vivo") -> byId("vivo_generic")
            else -> byId("auto")
        }
    }
}