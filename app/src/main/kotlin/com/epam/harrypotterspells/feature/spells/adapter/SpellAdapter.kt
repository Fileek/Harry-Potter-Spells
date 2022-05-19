package com.epam.harrypotterspells.feature.spells.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.epam.harrypotterspells.R
import com.epam.harrypotterspells.databinding.ItemSpellBinding
import com.epam.harrypotterspells.entity.SpannedSpell
import com.epam.harrypotterspells.feature.spells.SpellsFragmentDirections

class SpellAdapter : ListAdapter<SpannedSpell, SpellAdapter.SpellViewHolder>(itemComparator) {

    @ColorInt
    private var defaultColor: Int? = null
    private var colorMap: Map<String, Int>? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        defaultColor = ContextCompat.getColor(recyclerView.context, R.color.turquoise)
        colorMap = ColorMap.value.mapValues {
            ContextCompat.getColor(recyclerView.context, it.value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
        return createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun createViewHolder(viewGroup: ViewGroup): SpellViewHolder {
        val binding = getBinding(viewGroup)
        return SpellViewHolder(binding, colorMap, defaultColor)
    }

    private fun getBinding(viewGroup: ViewGroup): ItemSpellBinding {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return ItemSpellBinding.inflate(layoutInflater, viewGroup, false)
    }

    companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<SpannedSpell>() {
            override fun areItemsTheSame(oldItem: SpannedSpell, newItem: SpannedSpell) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SpannedSpell, newItem: SpannedSpell) =
                oldItem == newItem

            override fun getChangePayload(oldItem: SpannedSpell, newItem: SpannedSpell) = Any()
        }
    }

    class SpellViewHolder(
        private val binding: ItemSpellBinding,
        private val colorMap: Map<String, Int>?,
        @ColorInt private val defaultColor: Int?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(spell: SpannedSpell) {
            setListener(spell)
            renderColor(spell.light)
            renderSpell(spell)
        }

        private fun setListener(spell: SpannedSpell) {
            itemView.setOnClickListener {
                val action = SpellsFragmentDirections.detailsAction(spell.toSpell())
                it.findNavController().navigate(action)
            }
        }

        private fun renderColor(light: String) {
            val color = colorMap?.get(light) ?: defaultColor
            color?.let { binding.root.strokeColor = it }
        }

        private fun renderSpell(spell: SpannedSpell) = binding.run {
            name.text = spell.name
            incantation.text = spell.incantation
            type.text = spell.type
            effect.text = spell.effect
        }
    }
}