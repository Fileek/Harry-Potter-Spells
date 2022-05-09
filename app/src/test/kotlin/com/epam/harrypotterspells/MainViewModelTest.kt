package com.epam.harrypotterspells

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.domain.SwitchToLocalUseCase
import com.epam.harrypotterspells.domain.SwitchToRemoteUseCase
import com.epam.harrypotterspells.main.MainIntent
import com.epam.harrypotterspells.main.MainViewModel
import com.epam.harrypotterspells.main.MainViewState
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var viewModel: MainViewModel

    private lateinit var testObserver: TestObserver<MainViewState>

    private val switchToLocalIntent = MainIntent.SwitchToLocalIntent
    private val switchToRemoteIntent = MainIntent.SwitchToRemoteIntent

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = MainViewModel(
            TestSchedulerProvider(),
            SwitchToRemoteUseCase(repository),
            SwitchToLocalUseCase(repository)
        )
        testObserver = viewModel.getStates().test()
    }

    @Test
    fun `check that initialState returns only once`() {
        testObserver.assertValueCount(1)
    }

    @Test
    fun `check that initialState returns correct state`() {
        testObserver.assertValueAt(INITIAL_STATE_INDEX, MainViewState(isRemote = true))
    }

    @Test
    fun `check that SwitchToLocalIntent returns correct state`() {
        viewModel.processIntents(
            Observable.just(switchToLocalIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_SWITCH_TO_LOCAL_STATE_INDEX, MainViewState(isRemote = false))
    }

    @Test
    fun `check that SwitchToRemoteIntent returns correct state`() {
        viewModel.processIntents(
            Observable.just(switchToRemoteIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_SWITCH_TO_REMOTE_STATE_INDEX, MainViewState(isRemote = true))
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

    companion object {
        const val INITIAL_STATE_INDEX = 0
        private const val AFTER_SWITCH_TO_LOCAL_STATE_INDEX = 1
        private const val AFTER_SWITCH_TO_REMOTE_STATE_INDEX = 0
    }
}