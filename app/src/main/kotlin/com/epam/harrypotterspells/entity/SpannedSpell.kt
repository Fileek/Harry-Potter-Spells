package com.epam.harrypotterspells.entity

import android.text.SpannableString
import android.text.SpannedString

data class SpannedSpell(
    val id: String,
    val name: SpannedString,
    val incantation: SpannedString,
    val type: SpannedString,
    val effect: SpannedString,
    val light: String,
    val creator: String,
    val canBeVerbal: String,
) {
    fun toSpell() =
        Spell(
            id,
            name.toString(),
            incantation.toString(),
            type.toString(),
            effect.toString(),
            light, creator, canBeVerbal
        )

    fun toSpannableSpell() =
        SpannableSpell(
            id,
            SpannableString(name),
            SpannableString(incantation),
            SpannableString(type),
            SpannableString(effect),
            light, creator, canBeVerbal
        )

    fun containsString(string: String): Boolean {
        return name.contains(string, ignoreCase = true) ||
                incantation.contains(string, ignoreCase = true) ||
                type.contains(string, ignoreCase = true) ||
                effect.contains(string, ignoreCase = true)
    }
}
