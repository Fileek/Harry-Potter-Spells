package com.epam.harrypotterspells.data.repository.local

import org.junit.Assert.assertEquals
import org.junit.Test

class LocalRepositoryTest {

    private val repo = LocalRepository()
    private val spellsObserver = repo.getSpells().test()
    private val expectedSpells = StubList.spells.map { it.toSpell() }

    @Test
    fun `check that getSpells returns correct list of spells`() {
        spellsObserver.assertValue(expectedSpells)
    }

    @Test
    fun `check that saveSpell replaces given spell in spells list`() {
        val newSpell = expectedSpells.last().copy(name = "test")
        repo.saveSpell(newSpell)
        val actualSpell = spellsObserver.values().last().last()
        assertEquals(newSpell, actualSpell)
    }
}