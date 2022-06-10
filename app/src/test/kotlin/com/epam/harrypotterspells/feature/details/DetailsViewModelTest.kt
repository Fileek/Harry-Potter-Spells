package com.epam.harrypotterspells.feature.details

import androidx.lifecycle.SavedStateHandle
import com.epam.harrypotterspells.data.repository.local.StubData
import io.mockk.MockKAnnotations
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailsViewModelTest {

    private lateinit var viewModel: DetailsViewModel
    private lateinit var testObserver: TestObserver<DetailsViewState>
    private val testSpell = StubData.spells.last().toSpell()
    private val initialState = DetailsViewState(testSpell)
    private val testField = SpellField.INCANTATION
    private val editIntent = DetailsIntent.EditSpellFieldIntent(testField)

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = DetailsViewModel(
            state = SavedStateHandle(mapOf("spell" to testSpell)),
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
                isInitial = false,
                editableFields = setOf(testField),
                focus = testField
            )
        viewModel.processIntents(
            Observable.just(editIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_EDIT_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that several EditSpellFieldIntents return correct state`() {
        val secondField = SpellField.LIGHT
        val thirdField = SpellField.CREATOR
        val expectedState =
            initialState.copy(
                isInitial = false,
                editableFields = setOf(testField, secondField, thirdField),
                focus = thirdField
            )
        val secondEditIntent = DetailsIntent.EditSpellFieldIntent(secondField)
        val thirdEditIntent = DetailsIntent.EditSpellFieldIntent(thirdField)
        viewModel.processIntents(
            Observable.just(editIntent, secondEditIntent, thirdEditIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_SEVERAL_EDIT_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that several EditSpellFieldIntents and one SaveSpellFieldIntent return correct state`() {
        val secondField = SpellField.LIGHT
        val thirdField = SpellField.CREATOR
        val secondEditIntent = DetailsIntent.EditSpellFieldIntent(secondField)
        val thirdEditIntent = DetailsIntent.EditSpellFieldIntent(thirdField)
        val saveIntent = DetailsIntent.SaveSpellFieldIntent(testSpell, thirdField)
        val expectedState =
            initialState.copy(
                isInitial = false,
                editableFields = setOf(testField, secondField),
                focus = secondField
            )
        viewModel.processIntents(
            Observable.just(editIntent, secondEditIntent, thirdEditIntent, saveIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_EDIT_AND_SAVE_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that FocusOnFieldIntent returns correct state`() {
        val expectedState =
            initialState.copy(
                isInitial = false,
                editableFields = setOf(testField),
                focus = testField
            )
        viewModel.processIntents(
            Observable.just(editIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_EDIT_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that SaveSpellFieldIntent returns correct state`() {
        val newSpell = testSpell.copy(incantation = "incantation")
        val saveIntent = DetailsIntent.SaveSpellFieldIntent(newSpell, testField)
        val expectedState =
            initialState.copy(
                spell = newSpell,
                editableFields = emptySet(),
                isInitial = false,
                focus = null
            )
        viewModel.processIntents(
            Observable.just(editIntent, saveIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_UPDATE_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that same intent update state only once`() {
        val intentsSubject = BehaviorSubject.create<DetailsIntent>()
        val saveIntent = DetailsIntent.SaveSpellFieldIntent(testSpell, testField)
        viewModel.processIntents(
            intentsSubject.serialize()
        )
        intentsSubject.onNext(editIntent)
        intentsSubject.onNext(editIntent)
        intentsSubject.onNext(editIntent)
        testObserver.assertValueCount(2)

        intentsSubject.onNext(saveIntent)
        intentsSubject.onNext(saveIntent)
        intentsSubject.onNext(saveIntent)
        testObserver.assertValueCount(3)
    }

    @After
    fun clearObservers() {
        testObserver.dispose()
    }

    private companion object {
        private const val AFTER_EDIT_STATE_INDEX = 1
        private const val AFTER_UPDATE_STATE_INDEX = 2
        private const val AFTER_SEVERAL_EDIT_STATE_INDEX = 3
        private const val AFTER_EDIT_AND_SAVE_STATE_INDEX = 4
    }
}