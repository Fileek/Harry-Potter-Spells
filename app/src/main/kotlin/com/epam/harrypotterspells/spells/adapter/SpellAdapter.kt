package com.epam.harrypotterspells.spells.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.spells.SpellsFragmentDirections
import com.example.harrypotterspells.databinding.ItemSpellBinding

class SpellAdapter : ListAdapter<Spell, SpellViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
        return createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun createViewHolder(viewGroup: ViewGroup): SpellViewHolder {
        val binding = getBinding(viewGroup)
        val holder = SpellViewHolder(binding)
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
            val action = SpellsFragmentDirections.detailsAction(spell.id)
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