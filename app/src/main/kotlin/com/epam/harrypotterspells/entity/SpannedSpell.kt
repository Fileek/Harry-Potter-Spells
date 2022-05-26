package com.epam.harrypotterspells.entity

import android.text.SpannedString

data class SpannedSpell(
    val id: String,
    val name: SpannedString,
    val incantation: SpannedString,
    val type: SpannedString,
    val effect: SpannedString,
    val light: String,
    val creator: String,
    val canBeVerbal: CanBeVerbal,
) {
    fun toSpell() =
        Spell(
            id = id,
            name = name.toString(),
            incantation = incantation.toString(),
            type = type.toString(),
            effect = effect.toString(),
            light = light,
            creator = creator,
            canBeVerbal = canBeVerbal
        )
}
