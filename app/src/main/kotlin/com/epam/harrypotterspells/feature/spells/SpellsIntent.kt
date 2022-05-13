package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class SpellsIntent : MVIIntent {
    object LoadIntent : SpellsIntent()
}
