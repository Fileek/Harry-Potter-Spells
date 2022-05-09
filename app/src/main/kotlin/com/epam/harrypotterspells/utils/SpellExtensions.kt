package com.epam.harrypotterspells.utils

import com.epam.harrypotterspells.entities.JsonSpell
import com.epam.harrypotterspells.entities.Spell

private const val INCANTATION_STUB = "Unknown"
private const val CAN_BE_VERBAL = "Yes"
private const val CAN_NOT_BE_VERBAL = "No"
private const val CAN_BE_VERBAL_STUB = "Unknown"
private const val CREATOR_STUB = "Unknown"

fun JsonSpell.toSpell(): Spell {
    return Spell(
        id = this.id,
        name = this.name,
        incantation = this.incantation ?: INCANTATION_STUB,
        effect = this.effect,
        canBeVerbal = when (this.canBeVerbal) {
            true -> CAN_BE_VERBAL
            false -> CAN_NOT_BE_VERBAL
            null -> CAN_BE_VERBAL_STUB
        },
        type = this.type,
        light = this.light,
        creator = this.creator ?: CREATOR_STUB
    )
}

fun Spell.toJsonSpell(): JsonSpell {
    return JsonSpell(
        id = this.id,
        name = this.name,
        incantation = this.incantation,
        effect = this.effect,
        canBeVerbal = when (this.canBeVerbal) {
            CAN_BE_VERBAL -> true
            CAN_NOT_BE_VERBAL -> false
            else -> null
        },
        type = this.type,
        light = this.light,
        creator = this.creator
    )
}