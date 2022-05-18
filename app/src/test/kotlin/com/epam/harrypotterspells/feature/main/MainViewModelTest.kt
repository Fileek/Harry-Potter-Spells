package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.domain.SearchUseCase
import com.epam.harrypotterspells.domain.SwitchSourceUseCase
import com.epam.harrypotterspells.feature.main.MainIntent.SwitchSourceIntent
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var viewModel: MainViewModel

    private lateinit var testObserver: TestObserver<MainViewState>

    private val initialState = MainViewState()

    private val testString = "test"
    private val switchToLocalIntent = SwitchSourceIntent.ToLocalIntent
    private val switchToRemoteIntent = SwitchSourceIntent.ToRemoteIntent

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = MainViewModel(
            SearchUseCase(repository),
            SwitchSourceUseCase(repository)
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
    fun `check that SearchQueryIntent returns correct state`() {
        viewModel.processIntents(
            Observable.just(MainIntent.SearchByQueryIntent(testString))
        )
        testObserver.await()
        testObserver.assertValueAt(FIRST_VIEW_STATE_INDEX, initialState)
    }

    @Test
    fun `check that SwitchToLocalIntent returns correct state`() {
        viewModel.processIntents(
            Observable.just(switchToLocalIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(SECOND_VIEW_STATE_INDEX, initialState.copy(isRemote = false))
    }

    @Test
    fun `check that SwitchToRemoteIntent returns correct state`() {
        viewModel.processIntents(
            Observable.just(switchToRemoteIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(FIRST_VIEW_STATE_INDEX, initialState.copy(isRemote = true))
    }

    @Test
    fun `check that same intent update state only once`() {
        val intentsSubject = BehaviorSubject.create<MainIntent>()
        viewModel.processIntents(
            intentsSubject.serialize()
        )
        intentsSubject.onNext(switchToLocalIntent)
        intentsSubject.onNext(switchToLocalIntent)
        intentsSubject.onNext(switchToLocalIntent)

        testObserver.assertValueCount(2)

        intentsSubject.onNext(switchToRemoteIntent)
        intentsSubject.onNext(switchToRemoteIntent)
        intentsSubject.onNext(switchToRemoteIntent)

        testObserver.assertValueCount(3)
    }

    private companion object {
        private const val FIRST_VIEW_STATE_INDEX = 0
        private const val SECOND_VIEW_STATE_INDEX = 1
    }
}