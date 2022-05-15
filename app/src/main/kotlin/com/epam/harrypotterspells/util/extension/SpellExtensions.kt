package com.epam.harrypotterspells.util.extension

import android.text.style.BackgroundColorSpan
import com.epam.harrypotterspells.entity.SpannedSpell

/**
 * Filters all [SpannedSpell] that contain given [string] as a substring
 * in their [SpannedSpell.name], [SpannedSpell.incantation], [SpannedSpell.type] or [SpannedSpell.effect]
 * and highlights the given [string] by setting blue [BackgroundColorSpan].
 *
 * @param ignoreCase `true` to ignore character case when comparing strings.
 */
fun List<SpannedSpell>.filterByStringAndHighlightIt(
    string: String,
    ignoreCase: Boolean
): List<SpannedSpell> {
    return filter { it.containsString(string, ignoreCase) }
        .map {
            val spannableSpell = it.toSpannableSpell()
            spannableSpell.highlightStrings(string, ignoreCase)
            spannableSpell.toSpannedSpell()
        }
}