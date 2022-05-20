package com.epam.harrypotterspells.entity

import android.os.Parcelable
import android.text.SpannedString
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
    val canBeVerbal: String,
) : Parcelable {

    fun toSpannedSpell() =
        SpannedSpell(
            id = this.id,
            name = SpannedString(this.name),
            incantation = SpannedString(this.incantation),
            type = SpannedString(this.type),
            effect = SpannedString(this.effect),
            light = this.light,
            creator = this.creator,
            canBeVerbal = this.canBeVerbal
        )
}
