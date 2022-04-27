package com.epam.harrypotterspells.spells.adapter

import androidx.recyclerview.widget.RecyclerView
import com.epam.harrypotterspells.entities.Spell
import com.example.harrypotterspells.databinding.ItemSpellBinding

class SpellViewHolder(
    private val binding: ItemSpellBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(spell: Spell) {
        binding.apply {
            name.text = spell.name
            incantation.text = spell.incantation ?: "Unknown"
            type.text = spell.type
            effect.text = spell.effect
        }
    }
}