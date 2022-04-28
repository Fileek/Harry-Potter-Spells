package com.epam.harrypotterspells.data.local

import com.epam.harrypotterspells.entities.JsonSpell

object StubList {
    val spells = listOf(
        JsonSpell("1", "Blue sparks", null, "Jet of blue sparks", true, "Charm", "Blue", null),
        JsonSpell("2", "Exploding Charm", "Bombarda", "Small explosion", true, "Charm", "Transparent", null),
        JsonSpell("3", "Bubble Spell", null, "Creates a stream of non-bursting bubbles", false, "Conjuration", "Transparent", null),
        JsonSpell("4", "Locking Spell", "Colloportus", "Locks doors", true, "Charm", "Transparent", null),
        JsonSpell("5", "Shrinking Charm", "Reducio", "Shrinks target", true, "Charm", "Purple", null),
    )
}