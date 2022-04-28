package com.epam.harrypotterspells.entities

data class Spell(
    val id: String,
    val name: String,
    val incantation: String,
    val effect: String,
    val canBeVerbal: String,
    val type: String,
    val light: SpellColor,
    val creator: String
)
