package com.epam.harrypotterspells.feature.details

import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.mvibase.MVIResult

sealed class DetailsResult : MVIResult {
    data class EditSpellFieldResult(val field: SpellField) : DetailsResult()
    data class SaveSpellFieldResult(val spell: Spell, val field: SpellField) : DetailsResult()
}
