package com.epam.harrypotterspells.spells.adapter

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.epam.harrypotterspells.entities.Spell
import com.example.harrypotterspells.databinding.ItemSpellBinding

class SpellViewHolder(
    private val binding: ItemSpellBinding,
    private val colorMap: Map<String, Int>,
    @ColorInt private val defaultColor: Int
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(spell: Spell) = binding.apply {
        root.strokeColor = colorMap[spell.light] ?: defaultColor
        name.text = spell.name
        incantation.text = spell.incantation
        type.text = spell.type
        effect.text = spell.effect
    }
}