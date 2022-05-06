package com.epam.harrypotterspells.features.details

import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditCreatorResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditEffectResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditIncantationResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditLightResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditTypeResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.UpdateCreatorResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.UpdateEffectResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.UpdateIncantationResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.UpdateLightResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.UpdateTypeResult
import io.reactivex.rxjava3.functions.BiFunction

class DetailsReducer {

    fun reduce() = BiFunction { state: DetailsViewState, result: DetailsResult ->
        when (result) {
            is DetailsResult.EditSpellResult -> reduceEditSpellResult(result, state)
            is DetailsResult.UpdateSpellResult -> reduceUpdateSpellResult(result, state)
        }
    }

    private fun reduceEditSpellResult(
        result: DetailsResult.EditSpellResult,
        previousState: DetailsViewState
    ): DetailsViewState {
        val state = previousState.copy(inputsNotInitialized = false)
        return when (result) {
            is EditIncantationResult -> reduceEditIncantationResult(state)
            is EditTypeResult -> reduceEditTypeResult(state)
            is EditEffectResult -> reduceEditEffectResult(state)
            is EditLightResult -> reduceEditLightResult(state)
            is EditCreatorResult -> reduceEditCreatorResult(state)
        }
    }

    private fun reduceEditIncantationResult(previousState: DetailsViewState) =
        previousState.copy(
            incantationIsEditing = true,
            focus = SpellInputFieldFocus.INCANTATION,
        )

    private fun reduceEditTypeResult(previousState: DetailsViewState) =
        previousState.copy(
            typeIsEditing = true,
            focus = SpellInputFieldFocus.TYPE,
        )

    private fun reduceEditEffectResult(previousState: DetailsViewState) =
        previousState.copy(
            effectIsEditing = true,
            focus = SpellInputFieldFocus.EFFECT,
        )

    private fun reduceEditLightResult(previousState: DetailsViewState) =
        previousState.copy(
            lightIsEditing = true,
            focus = SpellInputFieldFocus.LIGHT,
        )

    private fun reduceEditCreatorResult(previousState: DetailsViewState) =
        previousState.copy(
            creatorIsEditing = true,
            focus = SpellInputFieldFocus.CREATOR,
        )

    private fun reduceUpdateSpellResult(
        result: DetailsResult.UpdateSpellResult,
        previousState: DetailsViewState
    ): DetailsViewState {
        val state = previousState.copy(focus = SpellInputFieldFocus.NONE)
        return when (result) {
            is UpdateIncantationResult -> reduceUpdateIncantationResult(state, result)
            is UpdateTypeResult -> reduceUpdateTypeResult(state, result)
            is UpdateEffectResult -> reduceUpdateEffectResult(state, result)
            is UpdateLightResult -> reduceUpdateLightResult(state, result)
            is UpdateCreatorResult -> reduceUpdateCreatorResult(state, result)
        }
    }

    private fun reduceUpdateIncantationResult(
        previousState: DetailsViewState,
        result: UpdateIncantationResult
    ): DetailsViewState {
        val newSpell = previousState.spell?.copy(incantation = result.incantation)
        return previousState.copy(
            spell = newSpell,
            incantationIsEditing = false,
        )

    }

    private fun reduceUpdateTypeResult(
        previousState: DetailsViewState,
        result: UpdateTypeResult
    ): DetailsViewState {
        val newSpell = previousState.spell?.copy(type = result.type)
        return previousState.copy(
            spell = newSpell,
            typeIsEditing = false,
        )
    }

    private fun reduceUpdateEffectResult(
        previousState: DetailsViewState,
        result: UpdateEffectResult
    ): DetailsViewState {
        val newSpell = previousState.spell?.copy(effect = result.effect)
        return previousState.copy(
            spell = newSpell,
            effectIsEditing = false,
        )
    }

    private fun reduceUpdateLightResult(
        previousState: DetailsViewState,
        result: UpdateLightResult
    ): DetailsViewState {
        val newSpell = previousState.spell?.copy(light = result.light)
        return previousState.copy(
            spell = newSpell,
            lightIsEditing = false
        )
    }

    private fun reduceUpdateCreatorResult(
        previousState: DetailsViewState,
        result: UpdateCreatorResult
    ): DetailsViewState {
        val newSpell = previousState.spell?.copy(creator = result.creator)
        return previousState.copy(
            spell = newSpell,
            creatorIsEditing = false,
        )
    }
}

