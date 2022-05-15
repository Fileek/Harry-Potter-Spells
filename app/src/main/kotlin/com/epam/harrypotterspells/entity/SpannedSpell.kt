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

    /**
     * Returns `true` if this [SpannedSpell] contains the specified [string] as a substring
     * in [SpannedSpell.name], [SpannedSpell.incantation], [SpannedSpell.type] or [SpannedSpell.effect].
     *
     * @param ignoreCase `true` to ignore character case when comparing strings.
     */
    fun containsString(string: String, ignoreCase: Boolean): Boolean {
        return name.contains(string, ignoreCase) ||
                incantation.contains(string, ignoreCase) ||
                type.contains(string, ignoreCase) ||
                effect.contains(string, ignoreCase)
    }
}
