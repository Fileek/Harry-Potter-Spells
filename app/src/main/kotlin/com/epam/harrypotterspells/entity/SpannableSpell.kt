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

    fun highlightStrings(string: String) {
        clearSpans()
        if (name.contains(string, ignoreCase = true)) highlightStringInName(string)
        if (incantation.contains(string, ignoreCase = true)) highlightStringInIncantation(string)
        if (type.contains(string, ignoreCase = true)) highlightStringInType(string)
        if (effect.contains(string, ignoreCase = true)) highlightStringInEffect(string)
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