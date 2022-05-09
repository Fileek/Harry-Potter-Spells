package com.epam.harrypotterspells

import com.epam.harrypotterspells.MainViewModelTest.Companion.INITIAL_STATE_INDEX
import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.domain.LoadSpellsUseCase
import com.epam.harrypotterspells.utils.toSpell
import com.epam.harrypotterspells.features.spells.SpellsIntent
import com.epam.harrypotterspells.features.spells.SpellsViewModel
import com.epam.harrypotterspells.features.spells.SpellsViewState
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

    private val loadSpellsIntent = SpellsIntent.LoadSpellsIntent
    private val spells = StubList.spells.map { it.toSpell() }
    private val jsonSpells = StubList.spells

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SpellsViewModel(
            TestSchedulerProvider(),
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
        testObserver.assertValueAt(INITIAL_STATE_INDEX, SpellsViewState.Idle)
    }

    @Test
    fun `check that LoadSpellsIntent starts with correct state`() {
        every { repository.getSpells() } returns Observable.just(jsonSpells)
        viewModel.processIntents(
            Observable.just(loadSpellsIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(START_STATE_INDEX, SpellsViewState.Loading)
    }

    @Test
    fun `check that LoadSpellsIntent returns correct state`() {
        every { repository.getSpells() } returns Observable.just(jsonSpells)
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