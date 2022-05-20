package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class SpellsIntent : MVIIntent {
    object LoadIntent : SpellsIntent()
    object LoadRemoteIntent : SpellsIntent()
    object LoadLocalIntent : SpellsIntent()
    data class SearchByQueryIntent(val query: String): SpellsIntent()
}
