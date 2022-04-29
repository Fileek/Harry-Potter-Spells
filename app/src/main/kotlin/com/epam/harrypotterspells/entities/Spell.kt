package com.epam.harrypotterspells.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Spell(
    val id: String,
    val name: String,
    val incantation: String,
    val effect: String,
    val canBeVerbal: String,
    val type: String,
    val light: String,
    val creator: String
): Parcelable
