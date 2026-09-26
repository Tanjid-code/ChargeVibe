package com.tanjid.chargevibe.model

import android.content.Context
import android.graphics.drawable.Drawable
import com.tanjid.chargevibe.icons.CuteIconGenerator
import com.tanjid.chargevibe.icons.CuteIconInfo

object CuteIconRegistry {

    fun getAllIcons(): List<CuteIconInfo> = CuteIconGenerator.ALL_ICONS

    fun getCategories(): List<String> = CuteIconGenerator.CATEGORIES

    fun getByCategory(category: String): List<CuteIconInfo> =
        CuteIconGenerator.ALL_ICONS.filter { it.category == category }

    fun generateDrawable(context: Context, iconId: Int, sizePx: Int): Drawable =
        CuteIconGenerator.generateIcon(context, iconId, sizePx)
}