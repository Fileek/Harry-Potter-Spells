package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.domain.LoadLocalFilteredSpellsUseCase
import com.epam.harrypotterspells.domain.LoadRemoteFilteredSpellsUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.observers.TestObserver
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

    private val initialState = SpellsViewState()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SpellsViewModel(
            LoadRemoteFilteredSpellsUseCase(remoteRepository),
            LoadLocalFilteredSpellsUseCase(localRepository)
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

    @After
    fun clearObservers() {
        testObserver.dispose()
    }
}