package com.epam.harrypotterspells.data.repository.remote

import com.epam.harrypotterspells.data.api.SpellApi
import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.data.repository.local.StubList
import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.util.TestSchedulerProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RemoteRepositoryTest {

    @MockK
    lateinit var api: SpellApi
    private lateinit var repo: Repository
    private lateinit var spellsObserver: TestObserver<List<Spell>>
    private val testSpells = StubList.spells.take(3)
    private val expectedSpells = testSpells.map { it.toSpell() }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { api.getSpells() } returns Single.just(testSpells)
        repo = RemoteRepository(api, TestSchedulerProvider())
        spellsObserver = repo.getSpells().test()
    }

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

    @After
    fun clearObservers() {
        spellsObserver.dispose()
    }
}