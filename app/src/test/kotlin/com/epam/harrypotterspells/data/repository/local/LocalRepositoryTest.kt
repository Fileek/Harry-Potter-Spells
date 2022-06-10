package com.epam.harrypotterspells.data.repository.local

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalRepositoryTest {

    private val repo = LocalRepository()
    private val spellsObserver = repo.getSpells().test()
    private val spells = StubData.spells

    @Test
    fun `check that getSpells returns correct list of spells`() {
        spellsObserver.assertValue(spells)
    }

    @Test
    fun `check that saveSpell replaces given spell in spells list`() {
        val newSpell = spells.last().copy(name = "name")
        repo.saveSpell(newSpell)
        val actualSpell = spellsObserver.values().last().last()
        assertEquals(newSpell, actualSpell)
    }

    @After
    fun clearObservers() {
        spellsObserver.dispose()
    }
}