package com.epam.harrypotterspells.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.epam.harrypotterspells.network.Spell
import com.example.harrypotterspells.databinding.ItemSpellBinding

class SpellAdapter : ListAdapter<Spell, SpellViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSpellBinding.inflate(layoutInflater)
        return SpellViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {
        holder.bind(getItem(position))
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