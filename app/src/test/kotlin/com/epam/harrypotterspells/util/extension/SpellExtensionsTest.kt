package com.epam.harrypotterspells.util.extension

import com.epam.harrypotterspells.entity.JsonSpell
import org.junit.Assert.assertEquals
import org.junit.Test

class SpellExtensionsTest {

    private val testString = "test"

    private val jsonSpell =
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

    private val spell =
        Spell(
            id = testString,
            name = testString,
            incantation = testString,
            type = testString,
            effect = testString,
            light = testString,
            creator = testString,
            canBeVerbal = CAN_BE_VERBAL_STUB
        )

    @Test
    fun `check that toSpell returns spell with INCANTATION_STUB if incantation is null`() {
        val expectedSpell = spell.copy(incantation = INCANTATION_STUB)
        val actualJsonSpell = jsonSpell.copy(incantation = null)
        val actualSpell = actualJsonSpell.toSpell()
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toSpell returns spell with CREATOR_STUB if creator is null`() {
        val expectedSpell = spell.copy(creator = CREATOR_STUB)
        val actualJsonSpell = jsonSpell.copy(creator = null)
        val actualSpell = actualJsonSpell.toSpell()
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toSpell returns spell with CAN_BE_VERBAL_STUB if canBeVerbal is null`() {
        val expectedSpell = spell.copy(canBeVerbal = CAN_BE_VERBAL_STUB)
        val actualJsonSpell = jsonSpell.copy(canBeVerbal = null)
        val actualSpell = actualJsonSpell.toSpell()
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toSpell returns spell with CAN_BE_VERBAL if canBeVerbal is true`() {
        val expectedSpell = spell.copy(canBeVerbal = CAN_BE_VERBAL)
        val actualJsonSpell = jsonSpell.copy(canBeVerbal = true)
        val actualSpell = actualJsonSpell.toSpell()
        assertEquals(expectedSpell, actualSpell)
    }

    @Test
    fun `check that toSpell returns spell with CAN_NOT_BE_VERBAL if canBeVerbal is false`() {
        val expectedSpell = spell.copy(canBeVerbal = CAN_NOT_BE_VERBAL)
        val actualJsonSpell = jsonSpell.copy(canBeVerbal = false)
        val actualSpell = actualJsonSpell.toSpell()
        assertEquals(expectedSpell, actualSpell)
    }
}