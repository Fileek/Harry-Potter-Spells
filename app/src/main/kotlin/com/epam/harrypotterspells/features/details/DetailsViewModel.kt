package com.epam.harrypotterspells.features.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.domain.EditSpellUseCase
import com.epam.harrypotterspells.domain.GetSpellUseCase
import com.epam.harrypotterspells.domain.UpdateSpellUseCase
import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.ext.TAG
import com.epam.harrypotterspells.features.details.DetailsAction.*
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.*
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.*
import com.epam.harrypotterspells.features.details.DetailsIntent.*
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent.*
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent.*
import com.epam.harrypotterspells.features.details.DetailsResult.*
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.*
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.*
import com.epam.harrypotterspells.mvibase.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getSpellUseCase: GetSpellUseCase,
    private val editSpellUseCase: EditSpellUseCase,
    private val updateSpellUseCase: UpdateSpellUseCase,
) : ViewModel(), MVIViewModel<DetailsIntent, DetailsViewState> {

    private val intentsSubject = BehaviorSubject.create<DetailsIntent>()

    private val statesObservable by lazy { compose() }

    private val actionProcessor by lazy {
        ObservableTransformer<DetailsAction, DetailsResult> { actions ->
            Observable.merge(
                actions.ofType(GetSpellAction::class.java).compose(
                    getSpellUseCase.performAction()
                ),
                actions.ofType(EditSpellAction::class.java).compose(
                    editSpellUseCase.performAction()
                ),
                actions.ofType(UpdateSpellAction::class.java).compose(
                    updateSpellUseCase.performAction()
                ),
            )
        }
    }

    private fun compose(): Observable<DetailsViewState> {
        return intentsSubject
            .map(this::actionFromIntent)
            .compose(actionProcessor)
            .scan(DetailsViewState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: DetailsIntent): DetailsAction {
        Log.v(TAG, "actionFromIntent $intent")
        return when (intent) {
            is GetSpellIntent -> GetSpellAction(intent.id)
            is EditSpellIntent -> {
                when (intent) {
                    is EditIncantationIntent -> EditIncantationAction
                    is EditTypeIntent -> EditTypeAction
                    is EditEffectIntent -> EditEffectAction
                    is EditLightIntent -> EditLightAction
                    is EditCreatorIntent -> EditCreatorAction
                }
            }
            is UpdateSpellIntent -> {
                when (intent) {
                    is UpdateIncantationIntent -> UpdateIncantationAction(
                        intent.id,
                        intent.incantation
                    )
                    is UpdateTypeIntent -> UpdateTypeAction(intent.id, intent.type)
                    is UpdateEffectIntent -> UpdateEffectAction(intent.id, intent.effect)
                    is UpdateLightIntent -> UpdateLightAction(intent.id, intent.light)
                    is UpdateCreatorIntent -> UpdateCreatorAction(intent.id, intent.creator)
                }
            }
        }
    }

    override fun states(): Observable<DetailsViewState> = statesObservable

    override fun processIntents(observable: Observable<DetailsIntent>) {
        observable.subscribe(intentsSubject)
    }

    private companion object {

        private val reducer = BiFunction { state: DetailsViewState, result: DetailsResult ->
            Log.v(TAG, "reducer $result")
            when (result) {
                is GetSpellResult -> {
                    when (result) {
                        is GetSpellResult.Success -> state.copy(spell = result.spell)
                    }
                }
                is UpdateSpellResult -> {
                    when (result) {
                        is UpdateIncantationResult -> {
                            state.copy(
                                incantationIsEditing = false,
                                fieldFocus = FieldFocus.NONE
                            )
                        }
                        is UpdateTypeResult -> {
                            state.copy(
                                typeIsEditing = false,
                                fieldFocus = FieldFocus.NONE
                            )
                        }
                        is UpdateEffectResult -> {
                            state.copy(
                                effectIsEditing = false,
                                fieldFocus = FieldFocus.NONE
                            )
                        }
                        is UpdateLightResult -> {
                            state.copy(
                                lightIsEditing = false,
                                fieldFocus = FieldFocus.NONE
                            )
                        }
                        is UpdateCreatorResult -> {
                            state.copy(
                                creatorIsEditing = false,
                                fieldFocus = FieldFocus.NONE
                            )
                        }
                    }
                }
                is EditSpellResult -> {
                    when (result) {
                        is EditIncantationResult -> {
                            state.copy(
                                incantationIsEditing = true,
                                fieldFocus = FieldFocus.INCANTATION,
                                inputsNotInitialized = false
                            )
                        }
                        is EditTypeResult -> {
                            state.copy(
                                typeIsEditing = true,
                                fieldFocus = FieldFocus.TYPE,
                                inputsNotInitialized = false
                            )
                        }
                        is EditEffectResult -> {
                            state.copy(
                                effectIsEditing = true,
                                fieldFocus = FieldFocus.EFFECT,
                                inputsNotInitialized = false
                            )
                        }
                        is EditLightResult -> {
                            state.copy(
                                lightIsEditing = true,
                                fieldFocus = FieldFocus.LIGHT,
                                inputsNotInitialized = false
                            )
                        }
                        is EditCreatorResult -> {
                            state.copy(
                                creatorIsEditing = true,
                                fieldFocus = FieldFocus.CREATOR,
                                inputsNotInitialized = false
                            )
                        }
                    }
                }
            }
        }
    }
}