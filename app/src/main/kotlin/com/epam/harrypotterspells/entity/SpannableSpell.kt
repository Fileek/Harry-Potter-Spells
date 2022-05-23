package com.epam.harrypotterspells.entity

import android.graphics.Color
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.BackgroundColorSpan
import androidx.annotation.ColorInt
import androidx.core.text.clearSpans
import com.epam.harrypotterspells.util.extension.highlightString

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
     * by setting [BackgroundColorSpan] with given [color].
     * @param string string to highlight.
     * @param ignoreCase `true` to ignore character case when comparing strings.
     * @param color color to highlight.
     */
    fun highlightStrings(
        string: String,
        ignoreCase: Boolean,
        @ColorInt color: Int
    ) {
        clearSpans()
        if (name.contains(string, ignoreCase)) name.highlightString(string, ignoreCase, color)
        if (incantation.contains(string, ignoreCase)) incantation.highlightString(string, ignoreCase, color)
        if (type.contains(string, ignoreCase)) type.highlightString(string, ignoreCase, color)
        if (effect.contains(string, ignoreCase)) effect.highlightString(string, ignoreCase, color)
    }

    private fun clearSpans() {
        name.clearSpans()
        incantation.clearSpans()
        type.clearSpans()
        effect.clearSpans()
    }
}