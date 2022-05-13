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
    val canBeVerbal: String,
) : Parcelable
