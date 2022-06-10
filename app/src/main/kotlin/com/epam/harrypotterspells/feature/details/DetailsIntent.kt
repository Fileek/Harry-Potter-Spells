package com.epam.harrypotterspells.feature.details

import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class DetailsIntent : MVIIntent {
    data class EditSpellFieldIntent(val field: SpellField) : DetailsIntent()
    data class FocusOnFieldIntent(val field: SpellField) : DetailsIntent()
    data class SaveSpellFieldIntent(val newSpell: Spell, val field: SpellField) : DetailsIntent()
}
