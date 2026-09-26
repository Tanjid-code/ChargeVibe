package com.tanjid.chargevibe.model

data class ChargingDesign(
    val id: Int,
    val name: String,
    val type: DesignType,
    val drawableRes: Int? = null,
    val emoji: String? = null,
    val customImagePath: String? = null,
    var isSelected: Boolean = false
)

enum class DesignType {
    BUILTIN_DRAWABLE,
    EMOJI,
    CUSTOM_IMAGE
}