package com.epam.harrypotterspells.entity

import android.text.SpannedString

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
            id = this.id,
            name = this.name,
            incantation = this.incantation ?: INCANTATION_STUB,
            type = this.type,
            effect = this.effect,
            light = this.light,
            creator = this.creator ?: CREATOR_STUB,
            canBeVerbal = when (this.canBeVerbal) {
                true -> CAN_BE_VERBAL
                false -> CAN_NOT_BE_VERBAL
                null -> CAN_BE_VERBAL_STUB
            },
        )

    fun toSpannedSpell() =
        SpannedSpell(
            id = this.id,
            name = SpannedString(this.name),
            incantation = SpannedString(this.incantation ?: INCANTATION_STUB),
            type = SpannedString(this.type),
            effect = SpannedString(this.effect),
            light = this.light,
            creator = this.creator ?: CREATOR_STUB,
            canBeVerbal = when (this.canBeVerbal) {
                true -> CAN_BE_VERBAL
                false -> CAN_NOT_BE_VERBAL
                null -> CAN_BE_VERBAL_STUB
            },
        )

    private companion object {
        private const val INCANTATION_STUB = "Unknown"
        private const val CAN_BE_VERBAL = "Yes"
        private const val CAN_NOT_BE_VERBAL = "No"
        private const val CAN_BE_VERBAL_STUB = "Unknown"
        private const val CREATOR_STUB = "Unknown"
    }
}
