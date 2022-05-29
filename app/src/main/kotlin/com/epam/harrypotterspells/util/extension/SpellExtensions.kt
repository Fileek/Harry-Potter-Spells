package com.epam.harrypotterspells.util.extension

import android.graphics.Color
import androidx.annotation.ColorInt
import com.epam.harrypotterspells.entity.SpannedSpell
import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.util.span.SubstringHighlighter

/**
 * Highlight the given [substring] with the given [color]
 * in [Spell.name], [Spell.incantation], [Spell.type] and [Spell.effect],
 * if the [substring] is contained there ignoring character case.
 * @param substring substring to highlight
 * @param color color to highlight. By default [Color.BLUE]
 */
fun List<Spell>.highlightSubstring(
    substring: String,
    @ColorInt color: Int = Color.BLUE
): List<SpannedSpell> {
    val substringHighlighter = SubstringHighlighter(substring, color)
    return map { spell ->
        with(spell) {
            SpannedSpell(
                id = id,
                name = substringHighlighter.invoke(name),
                incantation = substringHighlighter.invoke(incantation),
                type = substringHighlighter.invoke(type),
                effect = substringHighlighter.invoke(effect),
                light = light,
                creator = creator,
                canBeVerbal = canBeVerbal
            )
        }
    }
}