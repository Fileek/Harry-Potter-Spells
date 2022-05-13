package com.epam.harrypotterspells.util.extension

import com.epam.harrypotterspells.entity.SpannedSpell

fun List<SpannedSpell>.filterByStringAndHighlightIt(string: String): List<SpannedSpell> {
    return filter { it.containsString(string) }
        .map {
            val spannableSpell = it.toSpannableSpell()
            spannableSpell.highlightStrings(string)
            spannableSpell.toSpannedSpell()
        }
}