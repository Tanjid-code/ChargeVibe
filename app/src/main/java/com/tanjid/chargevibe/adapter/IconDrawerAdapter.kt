package com.tanjid.chargevibe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tanjid.chargevibe.R
import com.tanjid.chargevibe.model.ChargingDesign
import com.tanjid.chargevibe.model.CuteIconRegistry
import com.tanjid.chargevibe.model.DesignType

/**
 * Adapter for the full-screen icon drawer (BottomSheet).
 * Supports two item types: category headers and icon cells.
 * Uses the same cute-icon rendering logic as DesignAdapter — >= 1000 IDs
 * go through CuteIconRegistry.generateDrawable().
 */
class IconDrawerAdapter(
    private val items: List<DrawerItem>,
    private val onIconSelected: (ChargingDesign) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ICON = 1
    }

    sealed class DrawerItem {
        data class Header(val title: String) : DrawerItem()
        data class Icon(val design: ChargingDesign) : DrawerItem()
    }

    class HeaderVH(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.categoryHeaderText)
    }

    class IconVH(view: View) : RecyclerView.ViewHolder(view) {
        val card: LinearLayout = view.findViewById(R.id.gridDesignCard)
        val icon: ImageView = view.findViewById(R.id.gridDesignIcon)
        val emoji: TextView = view.findViewById(R.id.gridDesignEmoji)
        val name: TextView = view.findViewById(R.id.gridDesignName)
    }

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is DrawerItem.Header -> TYPE_HEADER
        is DrawerItem.Icon -> TYPE_ICON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderVH(inflater.inflate(R.layout.item_design_category_header, parent, false))
            else -> IconVH(inflater.inflate(R.layout.item_design_grid, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is DrawerItem.Header -> {
                (holder as HeaderVH).text.text = item.title
            }
            is DrawerItem.Icon -> {
                val vh = holder as IconVH
                val design = item.design
                vh.name.text = design.name

                when (design.type) {
                    DesignType.BUILTIN_DRAWABLE -> {
                        vh.icon.visibility = View.VISIBLE
                        vh.emoji.visibility = View.GONE

                        if (design.id >= 1000) {
                            val cuteId = design.id - 1000
                            val sizePx = (52 * vh.icon.context.resources.displayMetrics.density).toInt()
                            val drawable = CuteIconRegistry.generateDrawable(vh.icon.context, cuteId, sizePx)
                            vh.icon.setImageDrawable(drawable)
                        } else {
                            design.drawableRes?.let { vh.icon.setImageResource(it) }
                        }
                    }
                    DesignType.EMOJI -> {
                        vh.icon.visibility = View.GONE
                        vh.emoji.visibility = View.VISIBLE
                        vh.emoji.text = design.emoji
                    }
                    DesignType.CUSTOM_IMAGE -> {
                        vh.icon.visibility = View.VISIBLE
                        vh.emoji.visibility = View.GONE
                        design.drawableRes?.let { vh.icon.setImageResource(it) }
                    }
                }

                vh.card.setOnClickListener { onIconSelected(design) }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}