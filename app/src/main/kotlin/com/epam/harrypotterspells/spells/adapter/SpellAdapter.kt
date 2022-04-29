package com.epam.harrypotterspells.spells.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.spells.SpellsFragmentDirections
import com.example.harrypotterspells.R
import com.example.harrypotterspells.databinding.ItemSpellBinding

class SpellAdapter(
    context: Context
) : ListAdapter<Spell, SpellViewHolder>(itemComparator) {

    private val colorMap = ColorMap.value.mapValues {
        ContextCompat.getColor(context, it.value)
    }

    @ColorInt
    private val defaultColor = ContextCompat.getColor(context, R.color.turquoise)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
        return createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun createViewHolder(viewGroup: ViewGroup): SpellViewHolder {
        val binding = getBinding(viewGroup)
        val holder = SpellViewHolder(binding, colorMap, defaultColor)
        setListener(holder)
        return holder
    }

    private fun getBinding(viewGroup: ViewGroup): ItemSpellBinding {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return ItemSpellBinding.inflate(layoutInflater, viewGroup, false)
    }

    private fun setListener(holder: SpellViewHolder) {
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val spell = getItem(position)
            val action = SpellsFragmentDirections.detailsAction(spell)
            it.findNavController().navigate(action)
        }
    }

    companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<Spell>() {
            override fun areItemsTheSame(oldItem: Spell, newItem: Spell) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Spell, newItem: Spell) =
                oldItem == newItem
        }
    }
}