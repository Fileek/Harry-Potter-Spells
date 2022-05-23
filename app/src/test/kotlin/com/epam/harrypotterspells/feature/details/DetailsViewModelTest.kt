package com.epam.harrypotterspells.feature.details

import androidx.lifecycle.SavedStateHandle
import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.data.repository.local.StubList
import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.domain.SaveSpellUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailsViewModelTest {

    @MockK
    lateinit var localRepository: LocalRepository

    @MockK
    lateinit var remoteRepository: RemoteRepository

    private lateinit var viewModel: DetailsViewModel
    private lateinit var testObserver: TestObserver<DetailsViewState>
    private val testSpell = StubList.spells.first().toSpell()
    private val initialState = DetailsViewState(testSpell)
    private val testField = SpellField.INCANTATION
    private val editSpellFieldIntent = DetailsIntent.EditSpellFieldIntent(testField)

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = DetailsViewModel(
            state = SavedStateHandle(mapOf("spell" to testSpell)),
            saveSpellUseCase = SaveSpellUseCase(localRepository, remoteRepository),
        )
        testObserver = viewModel.getStates().test()
    }

    @Test
    fun `check that initial state returns only once`() {
        testObserver.assertValueCount(1)
    }

    @Test
    fun `check that initial state returns correct state`() {
        testObserver.assertValue(initialState)
    }

    @Test
    fun `check that EditSpellFieldIntent returns correct state`() {
        val expectedState =
            initialState.copy(
                editTextsNotSet = false,
                fieldsNowEditing = setOf(testField),
                focus = testField
            )
        viewModel.processIntents(
            Observable.just(editSpellFieldIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_EDIT_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that FocusOnFieldIntent returns correct state`() {
        val expectedState =
            initialState.copy(
                editTextsNotSet = false,
                fieldsNowEditing = setOf(testField),
                focus = testField
            )
        viewModel.processIntents(
            Observable.just(editSpellFieldIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_EDIT_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that SaveSpellFieldIntent returns correct state`() {
        val newSpell = testSpell.copy(incantation = "test")
        val updateIntent = DetailsIntent.SaveSpellFieldIntent(newSpell, testField)
        val expectedState =
            initialState.copy(
                spell = newSpell,
                fieldsNowEditing = emptySet(),
                editTextsNotSet = false,
                focus = null
            )
        viewModel.processIntents(
            Observable.just(editSpellFieldIntent, updateIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_UPDATE_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that same intent update state only once`() {
        val intentsSubject = BehaviorSubject.create<DetailsIntent>()
        val updateIntent = DetailsIntent.SaveSpellFieldIntent(testSpell, testField)
        viewModel.processIntents(
            intentsSubject.serialize()
        )
        intentsSubject.onNext(editSpellFieldIntent)
        intentsSubject.onNext(editSpellFieldIntent)
        intentsSubject.onNext(editSpellFieldIntent)
        testObserver.assertValueCount(2)

        intentsSubject.onNext(updateIntent)
        intentsSubject.onNext(updateIntent)
        intentsSubject.onNext(updateIntent)
        testObserver.assertValueCount(3)
    }

    @After
    fun clearObservers() {
        testObserver.dispose()
    }

    private companion object {
        private const val AFTER_EDIT_STATE_INDEX = 1
        private const val AFTER_UPDATE_STATE_INDEX = 2
    }
}