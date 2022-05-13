package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.feature.spells.SpellsAction.LoadAction
import com.epam.harrypotterspells.feature.spells.SpellsResult.LoadResult
import com.epam.harrypotterspells.util.TestSchedulerProvider
import com.epam.harrypotterspells.util.extension.toSpell
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
    private lateinit var actionComposer: ActionComposer<LoadAction, LoadResult>

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
        val testObserver = actionComposer(LoadAction)
        testObserver.await()
        testObserver.assertValueAt(RESULT_INDEX, LoadResult.Success(spells))
    }

    @Test
    fun `check that LoadSpellsAction returns error result onError`() {
        val throwable = ConnectException()
        every { repository.getSpells() } returns Observable.error(throwable)
        val testObserver = actionComposer(LoadAction)
        testObserver.await()
        testObserver.assertValueAt(RESULT_INDEX, LoadResult.Error(throwable))
    }

    private companion object {
        private const val RESULT_INDEX = 1
    }
}