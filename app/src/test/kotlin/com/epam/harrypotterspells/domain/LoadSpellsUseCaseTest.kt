package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.features.spells.SpellsAction.LoadSpellsAction
import com.epam.harrypotterspells.features.spells.SpellsResult.LoadSpellsResult
import com.epam.harrypotterspells.utils.TestSchedulerProvider
import com.epam.harrypotterspells.utils.extensions.toSpell
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import java.net.ConnectException
import org.junit.Before
import org.junit.Test

class LoadSpellsUseCaseTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var useCase: LoadSpellsUseCase
    private lateinit var actionComposer: ActionComposer<LoadSpellsAction, LoadSpellsResult>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = LoadSpellsUseCase(repository, TestSchedulerProvider())
        actionComposer = ActionComposer(useCase)
    }

    @Test
    fun `check that LoadSpellsAction returns correct data onSuccess`() {
        val jsonSpells = StubList.spells
        val spells = jsonSpells.map { it.toSpell() }
        every { repository.getSpells() } returns Observable.just(jsonSpells)
        val testObserver = actionComposer(LoadSpellsAction)
        testObserver.await()
        testObserver.assertValueAt(RESULT_INDEX, LoadSpellsResult.Success(spells))
    }

    @Test
    fun `check that LoadSpellsAction returns error result onError`() {
        val throwable = ConnectException()
        every { repository.getSpells() } returns Observable.error(throwable)
        val testObserver = actionComposer(LoadSpellsAction)
        testObserver.await()
        testObserver.assertValueAt(RESULT_INDEX, LoadSpellsResult.Error(throwable))
    }

    private companion object {
        private const val RESULT_INDEX = 1
    }
}