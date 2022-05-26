package com.epam.harrypotterspells.util.span

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.BackgroundColorSpan
import androidx.annotation.ColorInt

/**
 * Encapsulates the [substring] with highlight parameters for later highlighting in other strings
 * using [BackgroundColorSpan] with the given [color].
 * @param substring substring to highlight.
 * @param ignoreCase `true` to ignore character case when matching a string. By default `true`.
 * @param color color to highlight. By default [Color.BLUE]
 */
class SubstringHighlighter(
    private val substring: String,
    private val ignoreCase: Boolean = true,
    @ColorInt private val color: Int = Color.BLUE
) {
    private val span = BackgroundColorSpan(color)
    private val flags = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE

    /**
     * If the given [string] contains [SubstringHighlighter.substring],
     * return [string] with highlighted substring inside,
     * otherwise just converts [string] to [SpannedString].
     * @param string string to span
     */
    operator fun invoke(string: String): SpannedString {
        return if (string.contains(other = substring, ignoreCase = ignoreCase))
            highlightString(string)
        else
            SpannedString(string)
    }

    private fun highlightString(string: String): SpannedString {
        val start = string.indexOf(string = substring, ignoreCase = ignoreCase)
        val end = start + substring.length
        val spannableString = SpannableString(string)
        spannableString.setSpan(span, start, end, flags)
        return SpannedString(spannableString)
    }
}