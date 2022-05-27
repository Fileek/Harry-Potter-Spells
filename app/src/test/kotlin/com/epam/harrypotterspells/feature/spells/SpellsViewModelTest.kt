package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.data.repository.local.StubData
import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.domain.GetLocalSpellsUseCase
import com.epam.harrypotterspells.domain.GetRemoteSpellsUseCase
import com.epam.harrypotterspells.domain.SaveLocalSpellUseCase
import com.epam.harrypotterspells.util.TestSchedulerProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import java.net.ConnectException
import java.util.Collections
import org.junit.After
import org.junit.Before
import org.junit.Test

class SpellsViewModelTest {

    @MockK
    lateinit var localRepository: LocalRepository

    @MockK
    lateinit var remoteRepository: RemoteRepository

    private lateinit var viewModel: SpellsViewModel
    private lateinit var testObserver: TestObserver<SpellsViewState>
    private val error = ConnectException()

    private val testString = "test"
    private val remoteJsonSpells = StubData.spells.take(3)
    private val remoteSpells = remoteJsonSpells.map { it.toSpell() }
    private val localJsonSpells = StubData.spells
    private val localSpells = localJsonSpells.map { it.toSpell() }
    private val initialState = SpellsViewState()
    private val loadingState = SpellsViewState(isLoading = true)
    private val errorState = SpellsViewState(error = error)
    private val remoteDataState = SpellsViewState(data = remoteSpells)
    private val localDataState = SpellsViewState(data = localSpells)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { remoteRepository.getSpells() } returns Single.just(remoteJsonSpells)
        every { localRepository.getSpells() } returns Single.just(localJsonSpells)
        viewModel = SpellsViewModel(
            TestSchedulerProvider(),
            GetRemoteSpellsUseCase(remoteRepository),
            GetLocalSpellsUseCase(localRepository),
            SaveLocalSpellUseCase(localRepository)
        )
        testObserver = viewModel.getStates().test()
    }

    @Test
    fun `check that initialState returns only once`() {
        testObserver.assertValueCount(1)
    }

    @Test
    fun `check that initialState returns correct state`() {
        testObserver.assertValue(initialState)
    }

    @Test
    fun `check that InitialIntent starts with loading state`() {
        viewModel.processIntents(
            Observable.just(SpellsIntent.InitialIntent)
        )
        testObserver.assertValueAt(SECOND_VIEW_STATE_INDEX, loadingState)
    }

    @Test
    fun `check that InitialIntent returns state with remote data on success`() {
        viewModel.processIntents(
            Observable.just(SpellsIntent.InitialIntent)
        )
        testObserver.assertValueAt(THIRD_VIEW_STATE_INDEX, remoteDataState)
    }

    @Test
    fun `check that InitialIntent returns error state on error`() {
        every { remoteRepository.getSpells() } returns Single.error(error)
        viewModel.processIntents(
            Observable.just(SpellsIntent.InitialIntent)
        )
        testObserver.assertValueAt(THIRD_VIEW_STATE_INDEX, errorState)
    }

    @Test
    fun `check that GetRemoteIntent starts with loading state`() {
        viewModel.processIntents(
            Observable.just(SpellsIntent.GetRemoteIntent)
        )
        testObserver.assertValueAt(SECOND_VIEW_STATE_INDEX, loadingState)
    }

    @Test
    fun `check that GetRemoteIntent returns state with remote data on success`() {
        viewModel.processIntents(
            Observable.just(SpellsIntent.GetRemoteIntent)
        )
        testObserver.assertValueAt(THIRD_VIEW_STATE_INDEX, remoteDataState)
    }

    @Test
    fun `check that GetRemoteIntent returns error state on error`() {
        every { remoteRepository.getSpells() } returns Single.error(error)
        viewModel.processIntents(
            Observable.just(SpellsIntent.GetRemoteIntent)
        )
        testObserver.assertValueAt(THIRD_VIEW_STATE_INDEX, errorState)
    }

    @Test
    fun `check that GetLocalIntent returns state with local data`() {
        viewModel.processIntents(
            Observable.just(SpellsIntent.GetLocalIntent)
        )
        testObserver.assertValueAt(SECOND_VIEW_STATE_INDEX, localDataState)
    }

    @Test
    fun `check that SearchByQueryIntent returns correct state`() {
        val filteredSpells = remoteSpells.filter {
            it.name.contains(other = testString, ignoreCase = true) ||
                    it.incantation.contains(other = testString, ignoreCase = true) ||
                    it.type.contains(other = testString, ignoreCase = true) ||
                    it.effect.contains(other = testString, ignoreCase = true)
        }
        val expectedState = SpellsViewState(searchQuery = testString, data = filteredSpells)
        viewModel.processIntents(
            Observable.just(SpellsIntent.SearchByQueryIntent(testString))
        )
        testObserver.assertValueAt(THIRD_VIEW_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that SaveSpellIntent returns correct state`() {
        val oldSpell = remoteSpells.last()
        val newSpell = oldSpell.copy(name = testString)
        val newData = remoteSpells
        Collections.replaceAll(newData, oldSpell, newSpell)
        val expectedState = SpellsViewState(
            data = newData
        )
        viewModel.processIntents(
            Observable.just(SpellsIntent.InitialIntent, SpellsIntent.SaveSpellIntent(newSpell))
        )
        testObserver.assertValueAt(FOURTH_VIEW_STATE_INDEX, expectedState)
    }

    @After
    fun clearObservers() {
        testObserver.dispose()
    }

    private companion object {
        private const val SECOND_VIEW_STATE_INDEX = 1
        private const val THIRD_VIEW_STATE_INDEX = 2
        private const val FOURTH_VIEW_STATE_INDEX = 3
    }
}