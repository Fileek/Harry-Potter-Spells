package com.epam.harrypotterspells.entity

import android.os.Parcelable
import com.epam.harrypotterspells.entity.JsonSpell.Companion.CREATOR_STUB
import com.epam.harrypotterspells.entity.JsonSpell.Companion.INCANTATION_STUB
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
            id = id,
            name = name,
            incantation = if (incantation == INCANTATION_STUB) null else incantation,
            type = type,
            effect = effect,
            light = light,
            creator = if (creator == CREATOR_STUB) null else creator,
            canBeVerbal = when (canBeVerbal) {
                CanBeVerbal.YES -> true
                CanBeVerbal.NO -> false
                CanBeVerbal.UNKNOWN -> null
            },
        )
}
