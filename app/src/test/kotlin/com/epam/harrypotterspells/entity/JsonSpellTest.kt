package com.epam.harrypotterspells.entity

import com.epam.harrypotterspells.entity.JsonSpell.Companion.CREATOR_STUB
import com.epam.harrypotterspells.entity.JsonSpell.Companion.INCANTATION_STUB
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonSpellTest {


    private val testString = "test"

    private val testSpell =
        Spell(
            id = testString,
            name = testString,
            incantation = testString,
            type = testString,
            effect = testString,
            light = testString,
            creator = testString,
            canBeVerbal = CanBeVerbal.UNKNOWN
        )

    private val testJsonSpell =
        JsonSpell(
            id = testString,
            name = testString,
            incantation = testString,
            type = testString,
            effect = testString,
            light = testString,
            creator = testString,
            canBeVerbal = null
        )

    @Test
    fun `check that toSpell returns correct Spell`() {
        val actualSpell = testJsonSpell.toSpell()
        val expectedSpell = testSpell
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toSpell returns correct Spell if incantation is null`() {
        val actualSpell = testJsonSpell.copy(incantation = null).toSpell()
        val expectedSpell = testSpell.copy(incantation = INCANTATION_STUB)
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toSpell returns correct Spell if creator is null`() {
        val actualSpell = testJsonSpell.copy(creator = null).toSpell()
        val expectedSpell = testSpell.copy(creator = CREATOR_STUB)
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toSpell returns correct Spell if canBeVerbal is true`() {
        val actualSpell = testJsonSpell.copy(canBeVerbal = true).toSpell()
        val expectedSpell = testSpell.copy(canBeVerbal = CanBeVerbal.YES)
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toSpell returns correct Spell if canBeVerbal is false`() {
        val actualSpell = testJsonSpell.copy(canBeVerbal = false).toSpell()
        val expectedSpell = testSpell.copy(canBeVerbal = CanBeVerbal.NO)
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toSpell returns correct Spell if canBeVerbal is null`() {
        val actualSpell = testJsonSpell.copy(canBeVerbal = null).toSpell()
        val expectedSpell = testSpell.copy(canBeVerbal = CanBeVerbal.UNKNOWN)
        assertEquals(expectedSpell, actualSpell)
    }
}