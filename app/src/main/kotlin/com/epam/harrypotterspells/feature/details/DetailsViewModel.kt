package com.epam.harrypotterspells.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.domain.UseCase
import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.feature.details.DetailsAction.EditAction
import com.epam.harrypotterspells.feature.details.DetailsAction.UpdateAction
import com.epam.harrypotterspells.feature.details.DetailsIntent.EditIntent
import com.epam.harrypotterspells.feature.details.DetailsIntent.UpdateIntent
import com.epam.harrypotterspells.feature.details.DetailsResult.EditResult
import com.epam.harrypotterspells.feature.details.DetailsResult.UpdateResult
import com.epam.harrypotterspells.mvibase.MVIViewModel
import com.epam.harrypotterspells.util.`typealias`.DetailsActionTransformer
import com.epam.harrypotterspells.util.`typealias`.DetailsReducer
import com.epam.harrypotterspells.util.scheduler.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    state: SavedStateHandle,
    private val schedulerProvider: SchedulerProvider,
    private val editUseCase: UseCase<EditAction, EditResult>,
    private val updateUseCase: UseCase<UpdateAction, UpdateResult>,
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
            .observeOn(schedulerProvider.computation())
            .map(this::getActionFromIntent)
            .compose(processActions())
            .scan(initialState, reducer)
            .observeOn(schedulerProvider.ui())
            .replay(VIEW_STATE_BUFFER_SIZE)
            .autoConnect()
            .startWithItem(initialState)
            .distinctUntilChanged()
    }

    private fun getActionFromIntent(intent: DetailsIntent) = when (intent) {
        is EditIntent -> getActionFromEditIntent(intent)
        is UpdateIntent -> getActionFromUpdateIntent(intent)
    }

    private fun getActionFromEditIntent(intent: EditIntent) = when (intent) {
        is EditIntent.IncantationIntent -> EditAction.IncantationAction
        is EditIntent.TypeIntent -> EditAction.TypeAction
        is EditIntent.EffectIntent -> EditAction.EffectAction
        is EditIntent.LightIntent -> EditAction.LightAction
        is EditIntent.CreatorIntent -> EditAction.CreatorAction
    }

    private fun getActionFromUpdateIntent(intent: UpdateIntent) = when (intent) {
        is UpdateIntent.IncantationIntent -> {
            UpdateAction.IncantationAction(intent.id, intent.incantation)
        }
        is UpdateIntent.TypeIntent -> {
            UpdateAction.TypeAction(intent.id, intent.type)
        }
        is UpdateIntent.EffectIntent -> {
            UpdateAction.EffectAction(intent.id, intent.effect)
        }
        is UpdateIntent.LightIntent -> {
            UpdateAction.LightAction(intent.id, intent.light)
        }
        is UpdateIntent.CreatorIntent -> {
            UpdateAction.CreatorAction(intent.id, intent.creator)
        }
    }

    private fun processActions() = DetailsActionTransformer { actions ->
        Observable.merge(
            actions.ofType(EditAction::class.java).compose(
                editUseCase.performAction()
            ),
            actions.ofType(UpdateAction::class.java).compose(
                updateUseCase.performAction()
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
        private val reducer = DetailsReducer { state, result ->
            when (result) {
                is EditResult -> {
                    val newState = state.copy(inputsTextsNotSet = false)
                    when (result) {
                        is EditResult.IncantationResult -> newState.copy(
                            incantationIsEditing = true,
                            focus = SpellFieldFocus.INCANTATION,
                        )
                        is EditResult.TypeResult -> newState.copy(
                            typeIsEditing = true,
                            focus = SpellFieldFocus.TYPE,
                        )
                        is EditResult.EffectResult -> newState.copy(
                            effectIsEditing = true,
                            focus = SpellFieldFocus.EFFECT,
                        )
                        is EditResult.LightResult -> newState.copy(
                            lightIsEditing = true,
                            focus = SpellFieldFocus.LIGHT,
                        )
                        is EditResult.CreatorResult -> newState.copy(
                            creatorIsEditing = true,
                            focus = SpellFieldFocus.CREATOR,
                        )
                    }
                }
                is UpdateResult -> {
                    val newState = state.copy(focus = SpellFieldFocus.NONE)
                    when (result) {
                        is UpdateResult.IncantationResult -> {
                            newState.copy(
                                spell = newState.spell?.copy(incantation = result.incantation),
                                incantationIsEditing = false,
                            )
                        }
                        is UpdateResult.TypeResult -> {
                            newState.copy(
                                spell = newState.spell?.copy(type = result.type),
                                typeIsEditing = false,
                            )
                        }
                        is UpdateResult.EffectResult -> {
                            newState.copy(
                                spell = newState.spell?.copy(effect = result.effect),
                                effectIsEditing = false,
                            )
                        }
                        is UpdateResult.LightResult -> {
                            newState.copy(
                                spell = newState.spell?.copy(light = result.light),
                                lightIsEditing = false
                            )
                        }
                        is UpdateResult.CreatorResult -> {
                            newState.copy(
                                spell = newState.spell?.copy(creator = result.creator),
                                creatorIsEditing = false,
                            )
                        }
                    }
                }
            }
        }
    }
}