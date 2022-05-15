package com.epam.harrypotterspells.entity

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.BackgroundColorSpan
import androidx.core.text.clearSpans

data class SpannableSpell(
    val id: String,
    val name: SpannableString,
    val incantation: SpannableString,
    val type: SpannableString,
    val effect: SpannableString,
    val light: String,
    val creator: String,
    val canBeVerbal: String,
) {
    fun toSpannedSpell() =
        SpannedSpell(
            id,
            SpannedString(name),
            SpannedString(incantation),
            SpannedString(type),
            SpannedString(effect),
            light,
            creator,
            canBeVerbal
        )

    /**
     * Highlights the given [string] in [SpannableSpell.name], [SpannableSpell.incantation],
     * [SpannableSpell.type] and [SpannableSpell.effect] if they contain it as a substring
     * by setting blue [BackgroundColorSpan].
     *
     * @param ignoreCase `true` to ignore character case when comparing strings.
     */
    fun highlightStrings(string: String, ignoreCase: Boolean) {
        clearSpans()
        if (name.contains(string, ignoreCase)) highlightStringInName(string)
        if (incantation.contains(string, ignoreCase)) highlightStringInIncantation(string)
        if (type.contains(string, ignoreCase)) highlightStringInType(string)
        if (effect.contains(string, ignoreCase)) highlightStringInEffect(string)
    }

    private fun clearSpans() {
        name.clearSpans()
        incantation.clearSpans()
        type.clearSpans()
        effect.clearSpans()
    }

    private fun highlightStringInName(string: String) {
        val start = name.indexOf(string, ignoreCase = true)
        val end = start + string.length
        name.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun highlightStringInIncantation(string: String) {
        val start = incantation.indexOf(string, ignoreCase = true)
        val end = start + string.length
        incantation.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun highlightStringInType(string: String) {
        val start = type.indexOf(string, ignoreCase = true)
        val end = start + string.length
        type.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun highlightStringInEffect(string: String) {
        val start = effect.indexOf(string, ignoreCase = true)
        val end = start + string.length
        effect.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private companion object {
        private val span = BackgroundColorSpan(Color.BLUE)
    }
}