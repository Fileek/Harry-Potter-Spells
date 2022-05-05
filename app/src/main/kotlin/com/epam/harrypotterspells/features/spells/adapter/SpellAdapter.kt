package com.epam.harrypotterspells.features.spells.adapter

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
import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.features.spells.SpellsFragmentDirections

class SpellAdapter : ListAdapter<Spell, SpellAdapter.SpellViewHolder>(itemComparator) {

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

        private val itemComparator = object : DiffUtil.ItemCallback<Spell>() {
            override fun areItemsTheSame(oldItem: Spell, newItem: Spell) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Spell, newItem: Spell) =
                oldItem == newItem
        }
    }

    class SpellViewHolder(
        private val binding: ItemSpellBinding,
        private val colorMap: Map<String, Int>?,
        @ColorInt private val defaultColor: Int?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(spell: Spell) {
            setListener(spell)
            renderColor(spell.light)
            renderSpell(spell)
        }

        private fun setListener(spell: Spell) {
            itemView.setOnClickListener {
                val action = SpellsFragmentDirections.detailsAction(spell.id)
                it.findNavController().navigate(action)
            }
        }

        private fun renderColor(light: String) {
            val color = colorMap?.get(light) ?: defaultColor
            color?.let { binding.root.strokeColor = it }
        }

        private fun renderSpell(spell: Spell) = binding.run {
            name.text = spell.name
            incantation.text = spell.incantation
            type.text = spell.type
            effect.text = spell.effect
        }
    }
}