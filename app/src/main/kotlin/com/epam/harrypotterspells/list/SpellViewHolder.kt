package com.epam.harrypotterspells.list

import androidx.recyclerview.widget.RecyclerView
import com.epam.harrypotterspells.network.Spell
import com.example.harrypotterspells.databinding.ItemSpellBinding

class SpellViewHolder(
    private val binding: ItemSpellBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(spell: Spell) {
        binding.apply {
            spellName.text = spell.name
            spellIncantation.text = spell.incantation ?: "Unknown"
            spellType.text = spell.type
            spellEffect.text = spell.effect
        }
    }
}