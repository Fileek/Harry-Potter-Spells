package com.epam.harrypotterspells.entity

import com.epam.harrypotterspells.entity.JsonSpell.Companion.CREATOR_STUB
import com.epam.harrypotterspells.entity.JsonSpell.Companion.INCANTATION_STUB
import org.junit.Assert.assertEquals
import org.junit.Test

class SpellTest {

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
    fun `check that toJsonSpell returns correct JsonSpell`() {
        val actualSpell = testSpell.toJsonSpell()
        val expectedSpell = testJsonSpell
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toJsonSpell returns correct JsonSpell if incantation is unknown`() {
        val actualSpell = testSpell.copy(incantation = INCANTATION_STUB).toJsonSpell()
        val expectedSpell = testJsonSpell.copy(incantation = null)
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toJsonSpell returns correct JsonSpell if creator is unknown`() {
        val actualSpell = testSpell.copy(creator = CREATOR_STUB).toJsonSpell()
        val expectedSpell = testJsonSpell.copy(creator = null)
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toJsonSpell returns correct JsonSpell if canBeVerbal is YES`() {
        val actualSpell = testSpell.copy(canBeVerbal = CanBeVerbal.YES).toJsonSpell()
        val expectedSpell = testJsonSpell.copy(canBeVerbal = true)
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toJsonSpell returns correct JsonSpell if canBeVerbal is NO`() {
        val actualSpell = testSpell.copy(canBeVerbal = CanBeVerbal.NO).toJsonSpell()
        val expectedSpell = testJsonSpell.copy(canBeVerbal = false)
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toJsonSpell returns correct JsonSpell if canBeVerbal is UNKNOWN`() {
        val actualSpell = testSpell.copy(canBeVerbal = CanBeVerbal.UNKNOWN).toJsonSpell()
        val expectedSpell = testJsonSpell.copy(canBeVerbal = null)
        assertEquals(expectedSpell, actualSpell)
    }
}