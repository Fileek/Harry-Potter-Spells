package com.epam.harrypotterspells.features.details

import androidx.lifecycle.SavedStateHandle
import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.domain.EditSpellUseCase
import com.epam.harrypotterspells.domain.UpdateSpellUseCase
import com.epam.harrypotterspells.utils.TestSchedulerProvider
import com.epam.harrypotterspells.utils.extensions.toSpell
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test

class DetailsViewModelTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var viewModel: DetailsViewModel
    private lateinit var testObserver: TestObserver<DetailsViewState>
    private val spell = StubList.spells.first().toSpell()
    private val initialState = DetailsViewState(spell)

    private val editIncantationIntent = DetailsIntent.EditSpellIntent.EditIncantationIntent
    private val editTypeIntent = DetailsIntent.EditSpellIntent.EditTypeIntent
    private val editEffectIntent = DetailsIntent.EditSpellIntent.EditEffectIntent
    private val editLightIntent = DetailsIntent.EditSpellIntent.EditLightIntent
    private val editCreatorIntent = DetailsIntent.EditSpellIntent.EditCreatorIntent

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = DetailsViewModel(
            state = SavedStateHandle(mapOf("spell" to spell)),
            reducer = DetailsReducer(),
            schedulerProvider = TestSchedulerProvider(),
            editSpellUseCase = EditSpellUseCase(),
            updateSpellUseCase = UpdateSpellUseCase(repository),
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
    fun `check that EditIncantationIntent returns correct state`() {
        val expectedState =
            initialState.copy(
                incantationIsEditing = true,
                inputsNotInitialized = false,
                focus = SpellInputFieldFocus.INCANTATION
            )
        viewModel.processIntents(
            Observable.just(editIncantationIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_EDIT_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that EditTypeIntent returns correct state`() {
        val expectedState =
            initialState.copy(
                typeIsEditing = true,
                inputsNotInitialized = false,
                focus = SpellInputFieldFocus.TYPE
            )
        viewModel.processIntents(
            Observable.just(editTypeIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_EDIT_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that EditEffectIntent returns correct state`() {
        val expectedState =
            initialState.copy(
                effectIsEditing = true,
                inputsNotInitialized = false,
                focus = SpellInputFieldFocus.EFFECT
            )
        viewModel.processIntents(
            Observable.just(editEffectIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_EDIT_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that EditLightIntent returns correct state`() {
        val expectedState =
            initialState.copy(
                lightIsEditing = true,
                inputsNotInitialized = false,
                focus = SpellInputFieldFocus.LIGHT
            )
        viewModel.processIntents(
            Observable.just(editLightIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_EDIT_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that EditCreatorIntent returns correct state`() {
        val expectedState =
            initialState.copy(
                creatorIsEditing = true,
                inputsNotInitialized = false,
                focus = SpellInputFieldFocus.CREATOR
            )
        viewModel.processIntents(
            Observable.just(editCreatorIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_EDIT_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that UpdateIncantationIntent returns correct state`() {
        val incantation = "Expecto Patronum"
        val updateIntent =
            DetailsIntent.UpdateSpellIntent.UpdateIncantationIntent(spell.id, incantation)
        val expectedState =
            initialState.copy(
                spell = spell.copy(incantation = incantation),
                inputsNotInitialized = false,
            )
        viewModel.processIntents(
            Observable.just(editIncantationIntent, updateIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_UPDATE_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that UpdateTypeIntent returns correct state`() {
        val type = "Charm"
        val updateIntent = DetailsIntent.UpdateSpellIntent.UpdateTypeIntent(spell.id, type)
        val expectedState =
            initialState.copy(
                spell = spell.copy(type = type),
                inputsNotInitialized = false,
            )
        viewModel.processIntents(
            Observable.just(editTypeIntent, updateIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_UPDATE_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that UpdateEffectIntent returns correct state`() {
        val effect = "Conjures a spirit guardian"
        val updateIntent = DetailsIntent.UpdateSpellIntent.UpdateEffectIntent(spell.id, effect)
        val expectedState =
            initialState.copy(
                spell = spell.copy(effect = effect),
                inputsNotInitialized = false,
            )
        viewModel.processIntents(
            Observable.just(editEffectIntent, updateIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_UPDATE_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that UpdateLightIntent returns correct state`() {
        val light = "Silver"
        val updateIntent = DetailsIntent.UpdateSpellIntent.UpdateLightIntent(spell.id, light)
        val expectedState =
            initialState.copy(
                spell = spell.copy(light = light),
                inputsNotInitialized = false,
            )
        viewModel.processIntents(
            Observable.just(editLightIntent, updateIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_UPDATE_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that UpdateCreatorIntent returns correct state`() {
        val creator = "Unknown"
        val updateIntent = DetailsIntent.UpdateSpellIntent.UpdateCreatorIntent(spell.id, creator)
        val expectedState =
            initialState.copy(
                spell = spell.copy(creator = creator),
                inputsNotInitialized = false,
            )
        viewModel.processIntents(
            Observable.just(editCreatorIntent, updateIntent)
        )
        testObserver.await()
        testObserver.assertValueAt(AFTER_UPDATE_STATE_INDEX, expectedState)
    }

    @Test
    fun `check that same intent update state only once`() {
        val intentsSubject = BehaviorSubject.create<DetailsIntent>()
        val updateIntent = DetailsIntent.UpdateSpellIntent.UpdateIncantationIntent(spell.id, "")
        viewModel.processIntents(
            intentsSubject.serialize()
        )
        intentsSubject.onNext(editIncantationIntent)
        intentsSubject.onNext(editIncantationIntent)
        intentsSubject.onNext(editIncantationIntent)
        testObserver.assertValueCount(2)

        intentsSubject.onNext(updateIntent)
        intentsSubject.onNext(updateIntent)
        intentsSubject.onNext(updateIntent)
        testObserver.assertValueCount(3)
    }

    private companion object {
        private const val AFTER_EDIT_STATE_INDEX = 1
        private const val AFTER_UPDATE_STATE_INDEX = 2
    }
}