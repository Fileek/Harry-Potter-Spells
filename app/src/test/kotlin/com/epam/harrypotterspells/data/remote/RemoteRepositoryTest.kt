package com.epam.harrypotterspells.data.remote

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.data.api.SpellApi
import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.entities.JsonSpell
import com.epam.harrypotterspells.utils.TestSchedulerProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import java.net.ConnectException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RemoteRepositoryTest {

    @MockK
    lateinit var api: SpellApi

    private val localSpells = StubList.spells
    private val remoteSpells = StubList.spells.drop(2)
    private val testString = "test"
    private val lastSpellId = remoteSpells.last().id

    private lateinit var repository: Repository
    private lateinit var testObserver: TestObserver<List<JsonSpell>>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = RemoteRepository(api, TestSchedulerProvider())
        every { api.getSpells() } returns Single.just(remoteSpells)
        testObserver = repository.getSpells().test()
    }

    @Test
    fun `check that getSpells returns correct data onSuccess`() {
        testObserver.assertValueAt(INITIAL_LIST_INDEX, remoteSpells)
    }

    @Test
    fun `check that getSpells returns stub data onError`() {
        every { api.getSpells() } returns Single.error(ConnectException())
        testObserver = repository.getSpells().test()
        testObserver.assertValueAt(INITIAL_LIST_INDEX, localSpells)
    }

    @Test
    fun `check that switchToRemote switches data to remote`() {
        repository.switchToRemote()
        testObserver.assertValueAt(AFTER_SWITCH_LIST_INDEX, remoteSpells)
    }

    @Test
    fun `check that switchToLocal switches data to local`() {
        repository.switchToLocal()
        testObserver.assertValueAt(AFTER_SWITCH_LIST_INDEX, localSpells)
    }

    @Test
    fun `check that updateIncantation updates the incantation of the corresponding spell in data`() {

        repository.updateIncantation(lastSpellId, testString)
        val spell = testObserver.values().last().last()
        assertEquals(testString, spell.incantation)
    }

    @Test
    fun `check that updateType updates the type of the corresponding spell in data`() {
        repository.updateType(lastSpellId, testString)
        val spell = testObserver.values().last().last()
        assertEquals(testString, spell.type)
    }

    @Test
    fun `check that updateEffect updates the effect of the corresponding spell in data`() {
        repository.updateEffect(lastSpellId, testString)
        val spell = testObserver.values().last().last()
        assertEquals(testString, spell.effect)
    }

    @Test
    fun `check that updateLight updates the light of the corresponding spell in data`() {
        repository.updateLight(lastSpellId, testString)
        val spell = testObserver.values().last().last()
        assertEquals(testString, spell.light)
    }

    @Test
    fun `check that updateCreator updates the creator of the corresponding spell in data`() {
        repository.updateCreator(lastSpellId, testString)
        val spell = testObserver.values().last().last()
        assertEquals(testString, spell.creator)
    }

    private companion object {
        private const val INITIAL_LIST_INDEX = 0
        private const val AFTER_SWITCH_LIST_INDEX = 1
    }
}