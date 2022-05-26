package com.epam.harrypotterspells.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Spell(
    val id: String,
    val name: String,
    val incantation: String,
    val type: String,
    val effect: String,
    val light: String,
    val creator: String,
    val canBeVerbal: CanBeVerbal,
) : Parcelable {

    fun toJsonSpell() =
        JsonSpell(
            id = this.id,
            name = this.name,
            incantation = this.incantation,
            type = this.type,
            effect = this.effect,
            light = this.light,
            creator = this.creator,
            canBeVerbal = when (this.canBeVerbal) {
                CanBeVerbal.YES -> true
                CanBeVerbal.NO -> false
                CanBeVerbal.UNKNOWN -> null
            },
        )
}
