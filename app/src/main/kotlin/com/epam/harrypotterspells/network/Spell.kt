package com.epam.harrypotterspells.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Spell(
    val id: String,
    val name: String,
    var incantation: String?,
    val effect: String,
    val canBeVerbal: Boolean?,
    val type: String,
    val light: String,
    var creator: String?
) : Parcelable
