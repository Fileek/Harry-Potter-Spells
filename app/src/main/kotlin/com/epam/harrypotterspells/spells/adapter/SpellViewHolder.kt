package com.epam.harrypotterspells.spells.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.entities.SpellColor
import com.example.harrypotterspells.R
import com.epam.harrypotterspells.entities.SpellColor.*
import com.example.harrypotterspells.databinding.ItemSpellBinding

class SpellViewHolder(
    private val binding: ItemSpellBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(spell: Spell) {

        binding.root.strokeColor = getColor(spell.light)

        binding.apply {
            name.text = spell.name
            incantation.text = spell.incantation ?: "Unknown"
            type.text = spell.type
            effect.text = spell.effect
        }
    }

    private fun getColor(spellColor: SpellColor): Int {
        return ContextCompat.getColor(
            binding.root.context,
            when (spellColor) {
                Red -> R.color.red
                DarkRed -> R.color.dark_red
                FieryScarlet -> R.color.fiery_scarlet
                Scarlet -> R.color.scarlet
                Fire -> R.color.fire
                Orange -> R.color.orange
                Yellow -> R.color.yellow
                BrightYellow -> R.color.bright_yellow
                Gold -> R.color.gold
                Green -> R.color.green
                Turquoise -> R.color.turquoise
                Blue -> R.color.blue
                IcyBlue -> R.color.icy_blue
                BrightBlue -> R.color.bright_blue
                Violet -> R.color.violet
                Purple -> R.color.purple
                Pink -> R.color.pink
                White -> R.color.white
                BlueishWhite -> R.color.blueish_white
                Silver -> R.color.silver
                Grey -> R.color.grey
                BlackSmoke -> R.color.black_smoke
                Transparent -> R.color.turquoise
            }
        )
    }
}