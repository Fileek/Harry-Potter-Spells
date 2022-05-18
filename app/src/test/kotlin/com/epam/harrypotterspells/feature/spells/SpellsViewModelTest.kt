package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.domain.LoadSpellsUseCase
import com.epam.harrypotterspells.util.TestSchedulerProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class SpellsViewModelTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var viewModel: SpellsViewModel
    private lateinit var testObserver: TestObserver<SpellsViewState>

    private val schedulerProvider = TestSchedulerProvider()
    private val loadSpellsIntent = SpellsIntent.LoadIntent
    private val spells = StubList.spells.map { it.toSpannedSpell() }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SpellsViewModel(
            LoadSpellsUseCase(repository)
        )
        testObserver = viewModel.getStates().test()
    }

    @Test
    fun `check that initialState returns only once`() {
        testObserver.assertValueCount(1)
    }

    @Test
    fun `check that initialState returns correct state`() {
        testObserver.assertValue(SpellsViewState.Idle)
    }

    @Test
    fun `check that LoadSpellsIntent starts with correct state`() {
        every { repository.getSpells() } returns Observable.just(spells)
        viewModel.processIntents(
            Observable.just(loadSpellsIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(START_STATE_INDEX, SpellsViewState.Loading)
    }

    @Test
    fun `check that LoadSpellsIntent returns correct state`() {
        every { repository.getSpells() } returns Observable.just(spells)
        viewModel.processIntents(
            Observable.just(loadSpellsIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(FINAL_STATE_INDEX, SpellsViewState.Success(spells))
    }

    @Test
    fun `check that LoadSpellsIntent returns error state when there is an error in the RxChain`() {
        val error = NoSuchFieldError()
        every { repository.getSpells() } returns Observable.error(error)
        viewModel.processIntents(
            Observable.just(loadSpellsIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(FINAL_STATE_INDEX, SpellsViewState.Error(error))
    }

    private companion object {
        private const val START_STATE_INDEX = 1
        private const val FINAL_STATE_INDEX = 2
    }
}