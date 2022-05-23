package com.epam.harrypotterspells.util.extension

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import androidx.annotation.ColorInt
import com.epam.harrypotterspells.entity.SpannedSpell
import com.epam.harrypotterspells.entity.Spell

/**
 * Filters all [Spell] that contain given [string] as a substring
 * in their [Spell.name], [Spell.incantation], [Spell.type] or [Spell.effect]
 * and highlights the given [string] by setting [BackgroundColorSpan] with given [color].
 * Returns list of [SpannedSpell].
 * @param string string to highlight.
 * @param ignoreCase `true` to ignore character case when comparing strings. By default `true`.
 * @param color color to highlight. By default [Color.BLUE].
 */
fun List<Spell>.filterByStringAndHighlightIt(
    string: String,
    ignoreCase: Boolean = true,
    color: Int = Color.BLUE
): List<SpannedSpell> {
    return map { it.toSpannedSpell() }
        .filter { it.containsString(string, ignoreCase) }
        .map {
            val spannableSpell = it.toSpannableSpell()
            spannableSpell.highlightStrings(string, ignoreCase, color)
            spannableSpell.toSpannedSpell()
        }
}

/**
 * Highlight given [string] in the [SpannableString]
 * by setting [BackgroundColorSpan] with given [color].
 * @param string string to highlight.
 * @param ignoreCase `true` to ignore character case when comparing strings.
 * @param color color to highlight.
 */
fun SpannableString.highlightString(
    string: String,
    ignoreCase: Boolean,
    @ColorInt color: Int
) {
    val start = indexOf(string, ignoreCase = ignoreCase)
    val end = start + string.length
    val span = BackgroundColorSpan(color)
    val flags = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    setSpan(span, start, end, flags)
}