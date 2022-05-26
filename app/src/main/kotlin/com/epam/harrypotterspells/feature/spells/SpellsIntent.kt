package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class SpellsIntent : MVIIntent {
    object InitialIntent : SpellsIntent()
    object GetRemoteIntent : SpellsIntent()
    object GetLocalIntent : SpellsIntent()
    data class SearchByQueryIntent(val query: String): SpellsIntent()
    data class SaveSpellIntent(val spell: Spell): SpellsIntent()
}
