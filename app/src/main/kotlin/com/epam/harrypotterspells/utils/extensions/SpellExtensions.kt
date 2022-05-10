package com.epam.harrypotterspells.utils.extensions

import com.epam.harrypotterspells.entities.JsonSpell
import com.epam.harrypotterspells.entities.Spell

const val INCANTATION_STUB = "Unknown"
const val CAN_BE_VERBAL = "Yes"
const val CAN_NOT_BE_VERBAL = "No"
const val CAN_BE_VERBAL_STUB = "Unknown"
const val CREATOR_STUB = "Unknown"

fun JsonSpell.toSpell(): Spell {
    return Spell(
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
}