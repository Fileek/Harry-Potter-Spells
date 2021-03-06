package com.epam.harrypotterspells.util.span

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.BackgroundColorSpan
import androidx.annotation.ColorInt

/**
 * Encapsulates the [substring] with highlight parameters for later highlighting in other strings
 * using [BackgroundColorSpan] with the given [color].
 * @param substring substring to highlight.
 * @param color color to highlight.
 */
class SubstringHighlighter(
    private val substring: String,
    @ColorInt private val color: Int
) {
    private val span = BackgroundColorSpan(color)
    private val flags = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE

    /**
     * If the given [string] contains [SubstringHighlighter.substring] (ignoring character case),
     * return [string] with highlighted substring inside,
     * otherwise just converts [string] to [SpannedString].
     * @param string string to span
     */
    operator fun invoke(string: String): SpannedString {
        return if (string.contains(other = substring, ignoreCase = true))
            highlightString(string)
        else
            SpannedString(string)
    }

    private fun highlightString(string: String): SpannedString {
        val start = string.indexOf(string = substring, ignoreCase = true)
        val end = start + substring.length
        val spannableString = SpannableString(string)
        spannableString.setSpan(span, start, end, flags)
        return SpannedString(spannableString)
    }
}