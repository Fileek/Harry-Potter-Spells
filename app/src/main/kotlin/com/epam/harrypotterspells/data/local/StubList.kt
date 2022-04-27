package com.epam.harrypotterspells.data.local

import com.epam.harrypotterspells.entities.Spell

object StubList {
    val spells = listOf(
        Spell("1", "Blue sparks", null, "Jet of blue sparks", true, "Charm", "Blue", null),
        Spell("2", "Exploding Charm", "Bombarda", "Small explosion", true, "Charm", "Transparent", null),
        Spell("3", "Bubble Spell", null, "Creates a stream of non-bursting bubbles", false, "Conjuration", "Transparent", null),
        Spell("4", "Locking Spell", "Colloportus", "Locks doors", true, "Charm", "Transparent", null),
        Spell("5", "Shrinking Charm", "Reducio", "Shrinks target", true, "Charm", "Purple", null),
    )
}