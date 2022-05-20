package com.epam.harrypotterspells.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.domain.UseCase
import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.feature.details.DetailsAction.AddInFieldsNowEditingAction
import com.epam.harrypotterspells.feature.details.DetailsAction.SaveSpellAction
import com.epam.harrypotterspells.feature.details.DetailsIntent.EditSpellFieldIntent
import com.epam.harrypotterspells.feature.details.DetailsIntent.FocusOnFieldIntent
import com.epam.harrypotterspells.feature.details.DetailsIntent.SaveSpellFieldIntent
import com.epam.harrypotterspells.feature.details.DetailsResult.EditSpellFieldResult
import com.epam.harrypotterspells.feature.details.DetailsResult.SaveSpellFieldResult
import com.epam.harrypotterspells.mvibase.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    state: SavedStateHandle,
    private val saveSpellUseCase: UseCase<SaveSpellAction, SaveSpellFieldResult>,
) : ViewModel(), MVIViewModel<DetailsIntent, DetailsViewState> {

    /**
     * Currently displayed spell received by [DetailsFragmentArgs]
     */
    private val spell: Spell = checkNotNull(state[SPELL_KEY]) { "Spell is not initialized" }

    private val intentsSubject = BehaviorSubject.create<DetailsIntent>()
    private val initialState = DetailsViewState(spell)
    private val statesObservable = compose()

    /**
     * Composes [DetailsViewState] based on received intents in [intentsSubject]
     */
    private fun compose(): Observable<DetailsViewState> {
        return intentsSubject
            .map(this::getActionFromIntent)
            .compose(processActions())
            .scan(initialState, reducer)
            .replay(VIEW_STATE_BUFFER_SIZE)
            .autoConnect()
            .distinctUntilChanged()
    }

    private fun getActionFromIntent(intent: DetailsIntent) = when (intent) {
        is EditSpellFieldIntent -> AddInFieldsNowEditingAction(intent.field)
        is FocusOnFieldIntent -> AddInFieldsNowEditingAction(intent.field)
        is SaveSpellFieldIntent -> SaveSpellAction(intent.newSpell, intent.field)
    }

    private fun processActions() = ObservableTransformer<DetailsAction, DetailsResult> { actions ->
        Observable.merge(
            actions.ofType(AddInFieldsNowEditingAction::class.java).map {
                EditSpellFieldResult(it.field)
            },
            actions.ofType(SaveSpellAction::class.java).compose(
                saveSpellUseCase.performAction()
            ),
        )
    }

    override fun processIntents(observable: Observable<DetailsIntent>) {
        observable.subscribe(intentsSubject)
    }

    override fun getStates(): Observable<DetailsViewState> = statesObservable

    private companion object {
        private const val SPELL_KEY = "spell"
        private const val VIEW_STATE_BUFFER_SIZE = 1

        /**
         * Returns new [DetailsViewState] by applying given [DetailsResult] on given [DetailsViewState].
         */
        private val reducer =
            BiFunction<DetailsViewState, DetailsResult, DetailsViewState> { state, result ->
                when (result) {
                    is EditSpellFieldResult -> {
                        state.copy(
                            editTextsNotSet = false,
                            fieldsNowEditing = state.fieldsNowEditing.plus(result.field),
                            focus = result.field,
                        )
                    }
                    is SaveSpellFieldResult -> {
                        state.copy(
                            spell = result.spell,
                            fieldsNowEditing = state.fieldsNowEditing.minus(result.field),
                            focus = null
                        )
                    }
                }
            }
    }
}