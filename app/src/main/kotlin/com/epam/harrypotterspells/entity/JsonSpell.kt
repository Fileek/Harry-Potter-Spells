package com.epam.harrypotterspells.entity

import androidx.annotation.Keep

@Keep
data class JsonSpell(
    val id: String,
    val name: String,
    val incantation: String?,
    val effect: String,
    val canBeVerbal: Boolean?,
    val type: String,
    val light: String,
    val creator: String?
) {
    fun toSpell() =
        Spell(
            id = id,
            name = name,
            incantation = incantation ?: INCANTATION_STUB,
            type = type,
            effect = effect,
            light = light,
            creator = creator ?: CREATOR_STUB,
            canBeVerbal = when (canBeVerbal) {
                true -> CanBeVerbal.YES
                false -> CanBeVerbal.NO
                null -> CanBeVerbal.UNKNOWN
            },
        )

    companion object {
        const val INCANTATION_STUB = "Unknown"
        const val CREATOR_STUB = "Unknown"
    }
}
