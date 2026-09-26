package com.tanjid.chargevibe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tanjid.chargevibe.R
import com.tanjid.chargevibe.adapter.IconDrawerAdapter
import com.tanjid.chargevibe.icons.CuteIconGenerator
import com.tanjid.chargevibe.model.ChargingDesign

/**
 * Full-screen bottom-up drawer showing all icons in a vertical grid.
 * Groups by category with headers. Doesn't touch the existing horizontal
 * RecyclerView — this is an additional entry point.
 */
class IconDrawerSheet(
    private val allDesigns: List<ChargingDesign>,
    private val onIconSelected: (ChargingDesign) -> Unit
) : BottomSheetDialogFragment() {

    companion object {
        private const val GRID_COLUMNS = 3
        private const val CUTE_ID_OFFSET = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.sheet_icon_drawer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.drawerRecyclerView)
        val items = buildItemList()

        val layoutManager = GridLayoutManager(context, GRID_COLUMNS)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (items[position] is IconDrawerAdapter.DrawerItem.Header) GRID_COLUMNS else 1
            }
        }
        rv.layoutManager = layoutManager

        rv.adapter = IconDrawerAdapter(items) { design ->
            onIconSelected(design)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        // Expand to (near) full screen so the drawer opens fully
        val dialog = dialog as? BottomSheetDialog ?: return
        val sheet = dialog.findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet
        ) ?: return
        val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(sheet)
        behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        sheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        sheet.requestLayout()
    }

    private fun buildItemList(): List<IconDrawerAdapter.DrawerItem> {
        val result = mutableListOf<IconDrawerAdapter.DrawerItem>()

        // Bucket 1: classic built-in drawables (IDs 0-6 in your setup)
        val classicDrawables = allDesigns.filter {
            it.type == com.tanjid.chargevibe.model.DesignType.BUILTIN_DRAWABLE &&
                    it.id < CUTE_ID_OFFSET
        }
        if (classicDrawables.isNotEmpty()) {
            result.add(IconDrawerAdapter.DrawerItem.Header("Classic Icons"))
            classicDrawables.forEach { result.add(IconDrawerAdapter.DrawerItem.Icon(it)) }
        }

        // Bucket 2: emoji designs (IDs 7-14 in your setup)
        val emojiDesigns = allDesigns.filter {
            it.type == com.tanjid.chargevibe.model.DesignType.EMOJI
        }
        if (emojiDesigns.isNotEmpty()) {
            result.add(IconDrawerAdapter.DrawerItem.Header("Emoji"))
            emojiDesigns.forEach { result.add(IconDrawerAdapter.DrawerItem.Icon(it)) }
        }

        // Bucket 3: cute generated icons, grouped by their own category
        val cuteDesigns = allDesigns.filter { it.id >= CUTE_ID_OFFSET }
        if (cuteDesigns.isNotEmpty()) {
            // Map cute pseudo-drawable ID back to CuteIconInfo.category
            val cuteInfoById = CuteIconGenerator.ALL_ICONS.associateBy { it.id }
            val grouped = cuteDesigns.groupBy { design ->
                val cuteId = design.id - CUTE_ID_OFFSET
                cuteInfoById[cuteId]?.category ?: "Other"
            }
            // Preserve category order defined in CuteIconGenerator
            CuteIconGenerator.CATEGORIES.forEach { category ->
                val group = grouped[category] ?: return@forEach
                if (group.isEmpty()) return@forEach
                result.add(IconDrawerAdapter.DrawerItem.Header(category))
                group.forEach { result.add(IconDrawerAdapter.DrawerItem.Icon(it)) }
            }
            // Anything not in the known categories (defensive)
            grouped.keys
                .filterNot { CuteIconGenerator.CATEGORIES.contains(it) }
                .forEach { unknownCategory ->
                    val group = grouped[unknownCategory] ?: return@forEach
                    result.add(IconDrawerAdapter.DrawerItem.Header(unknownCategory))
                    group.forEach { result.add(IconDrawerAdapter.DrawerItem.Icon(it)) }
                }
        }

        return result
    }
}