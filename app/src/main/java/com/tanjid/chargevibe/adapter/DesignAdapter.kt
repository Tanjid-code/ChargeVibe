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

class DesignAdapter(
    private val designs: MutableList<ChargingDesign>,
    private val onDesignSelected: (ChargingDesign) -> Unit
) : RecyclerView.Adapter<DesignAdapter.ViewHolder>() {

    private var selectedPosition = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: LinearLayout = view.findViewById(R.id.designCard)
        val icon: ImageView = view.findViewById(R.id.designIcon)
        val emoji: TextView = view.findViewById(R.id.designEmoji)
        val name: TextView = view.findViewById(R.id.designName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val design = designs[position]
        holder.name.text = design.name

        when (design.type) {
            DesignType.BUILTIN_DRAWABLE -> {
                holder.icon.visibility = View.VISIBLE
                holder.emoji.visibility = View.GONE

                // Check if it's a cute generated icon (IDs 1000+)
                if (design.id >= 1000) {
                    val cuteId = design.id - 1000
                    val sizePx = (48 * holder.icon.context.resources.displayMetrics.density).toInt()
                    val drawable = CuteIconRegistry.generateDrawable(holder.icon.context, cuteId, sizePx)
                    holder.icon.setImageDrawable(drawable)
                } else {
                    design.drawableRes?.let { holder.icon.setImageResource(it) }
                }
            }
            DesignType.EMOJI -> {
                holder.icon.visibility = View.GONE
                holder.emoji.visibility = View.VISIBLE
                holder.emoji.text = design.emoji
            }
            DesignType.CUSTOM_IMAGE -> {
                holder.icon.visibility = View.VISIBLE
                holder.emoji.visibility = View.GONE
                design.drawableRes?.let { holder.icon.setImageResource(it) }
            }
        }

        holder.card.background = if (position == selectedPosition) {
            holder.card.context.getDrawable(R.drawable.bg_design_selected)
        } else {
            holder.card.context.getDrawable(R.drawable.bg_design_card)
        }

        holder.card.setOnClickListener {
            val oldPos = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(oldPos)
            notifyItemChanged(selectedPosition)
            onDesignSelected(design)
        }
    }

    override fun getItemCount() = designs.size

    fun setSelectedById(id: Int) {
        val index = designs.indexOfFirst { it.id == id }
        if (index >= 0) {
            val oldPos = selectedPosition
            selectedPosition = index
            notifyItemChanged(oldPos)
            notifyItemChanged(selectedPosition)
        }
    }
}