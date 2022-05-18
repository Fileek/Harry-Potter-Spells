package com.epam.harrypotterspells.feature.details

import androidx.lifecycle.SavedStateHandle
import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.domain.EditUseCase
import com.epam.harrypotterspells.domain.UpdateUseCase
import com.epam.harrypotterspells.util.TestSchedulerProvider
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

    private val editIncantationIntent = DetailsIntent.EditIntent.IncantationIntent
    private val editTypeIntent = DetailsIntent.EditIntent.TypeIntent
    private val editEffectIntent = DetailsIntent.EditIntent.EffectIntent
    private val editLightIntent = DetailsIntent.EditIntent.LightIntent
    private val editCreatorIntent = DetailsIntent.EditIntent.CreatorIntent

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = DetailsViewModel(
            state = SavedStateHandle(mapOf("spell" to spell)),
            editUseCase = EditUseCase(),
            updateUseCase = UpdateUseCase(repository),
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
                inputsTextsNotSet = false,
                focus = SpellFieldFocus.INCANTATION
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
                inputsTextsNotSet = false,
                focus = SpellFieldFocus.TYPE
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
                inputsTextsNotSet = false,
                focus = SpellFieldFocus.EFFECT
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
                inputsTextsNotSet = false,
                focus = SpellFieldFocus.LIGHT
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
                inputsTextsNotSet = false,
                focus = SpellFieldFocus.CREATOR
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
            DetailsIntent.UpdateIntent.IncantationIntent(spell.id, incantation)
        val expectedState =
            initialState.copy(
                spell = spell.copy(incantation = incantation),
                inputsTextsNotSet = false,
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
        val updateIntent = DetailsIntent.UpdateIntent.TypeIntent(spell.id, type)
        val expectedState =
            initialState.copy(
                spell = spell.copy(type = type),
                inputsTextsNotSet = false,
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
        val updateIntent = DetailsIntent.UpdateIntent.EffectIntent(spell.id, effect)
        val expectedState =
            initialState.copy(
                spell = spell.copy(effect = effect),
                inputsTextsNotSet = false,
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
        val updateIntent = DetailsIntent.UpdateIntent.LightIntent(spell.id, light)
        val expectedState =
            initialState.copy(
                spell = spell.copy(light = light),
                inputsTextsNotSet = false,
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
        val updateIntent = DetailsIntent.UpdateIntent.CreatorIntent(spell.id, creator)
        val expectedState =
            initialState.copy(
                spell = spell.copy(creator = creator),
                inputsTextsNotSet = false,
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
        val updateIntent = DetailsIntent.UpdateIntent.IncantationIntent(spell.id, "")
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