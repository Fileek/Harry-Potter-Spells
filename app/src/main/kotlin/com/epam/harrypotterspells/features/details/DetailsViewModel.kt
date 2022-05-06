package com.epam.harrypotterspells.features.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.domain.EditSpellUseCase
import com.epam.harrypotterspells.domain.UpdateSpellUseCase
import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditCreatorAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditEffectAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditIncantationAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditLightAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditTypeAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateCreatorAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateEffectAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateIncantationAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateLightAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateTypeAction
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent.EditCreatorIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent.EditEffectIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent.EditIncantationIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent.EditLightIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent.EditTypeIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent.UpdateCreatorIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent.UpdateEffectIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent.UpdateIncantationIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent.UpdateLightIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent.UpdateTypeIntent
import com.epam.harrypotterspells.mvibase.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    state: SavedStateHandle,
    private val editSpellUseCase: EditSpellUseCase,
    private val updateSpellUseCase: UpdateSpellUseCase,
    private val reducer: DetailsReducer
) : ViewModel(), MVIViewModel<DetailsIntent, DetailsViewState> {

    private val spell: Spell = checkNotNull(state[SPELL_KEY]) { "Spell is not initialized" }

    private val initialState = DetailsViewState(spell)

    private val intentsSubject = BehaviorSubject.create<DetailsIntent>()

    private val statesObservable = compose()

    override fun getStates(): Observable<DetailsViewState> = statesObservable

    override fun processIntents(observable: Observable<DetailsIntent>) {
        observable.subscribe(intentsSubject)
    }

    private fun compose(): Observable<DetailsViewState> {
        return intentsSubject
            .observeOn(Schedulers.computation())
            .map(this::getActionFromIntent)
            .compose(processActions())
            .scan(initialState, reducer.reduce())
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .replay(1)
            .autoConnect(0)
            .startWithItem(initialState)
    }

    private fun processActions() =
        ObservableTransformer<DetailsAction, DetailsResult> { actions ->
            Observable.merge(
                actions.ofType(EditSpellAction::class.java).compose(
                    editSpellUseCase.performAction()
                ),
                actions.ofType(UpdateSpellAction::class.java).compose(
                    updateSpellUseCase.performAction()
                ),
            )
        }

    private fun getActionFromIntent(intent: DetailsIntent) = when (intent) {
        is EditSpellIntent -> getActionFromEditSpellIntent(intent)
        is UpdateSpellIntent -> getActionFromUpdateSpellIntent(intent)
    }

    private fun getActionFromEditSpellIntent(intent: EditSpellIntent) = when (intent) {
        is EditIncantationIntent -> EditIncantationAction
        is EditTypeIntent -> EditTypeAction
        is EditEffectIntent -> EditEffectAction
        is EditLightIntent -> EditLightAction
        is EditCreatorIntent -> EditCreatorAction
    }

    private fun getActionFromUpdateSpellIntent(intent: UpdateSpellIntent) = when (intent) {
        is UpdateIncantationIntent -> UpdateIncantationAction(intent.id, intent.incantation)
        is UpdateTypeIntent -> UpdateTypeAction(intent.id, intent.type)
        is UpdateEffectIntent -> UpdateEffectAction(intent.id, intent.effect)
        is UpdateLightIntent -> UpdateLightAction(intent.id, intent.light)
        is UpdateCreatorIntent -> UpdateCreatorAction(intent.id, intent.creator)
    }

    private companion object {
        private const val SPELL_KEY = "spell"
    }
}