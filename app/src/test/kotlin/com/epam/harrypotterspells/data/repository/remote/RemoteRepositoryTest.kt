package com.epam.harrypotterspells.data.repository.remote

import com.epam.harrypotterspells.data.api.SpellApi
import com.epam.harrypotterspells.data.repository.local.StubData
import com.epam.harrypotterspells.entity.JsonSpell
import com.epam.harrypotterspells.util.TestSchedulerProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import java.net.ConnectException
import org.junit.After
import org.junit.Before
import org.junit.Test

class RemoteRepositoryTest {

    @MockK
    lateinit var api: SpellApi

    private lateinit var repo: RemoteRepository
    private lateinit var spellsObserver: TestObserver<List<JsonSpell>>
    private val localSpells = StubData.spells
    private val spells = StubData.spells.take(3)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { api.getSpells() } returns Single.just(spells)
        repo = RemoteRepository(api, TestSchedulerProvider())
        spellsObserver = repo.getSpells().test()
    }

    @Test
    fun `check that getSpells returns correct list of spells`() {
        spellsObserver.assertValue(spells)
    }

    @Test
    fun `check that getSpells returns local list on error`() {
        val error = ConnectException()
        every { api.getSpells() } returns Single.error(error)
        spellsObserver = repo.getSpells().test()
        spellsObserver.assertValue(localSpells)
    }

    @After
    fun clearObservers() {
        spellsObserver.dispose()
    }
}