package com.epam.harrypotterspells.features.spells

import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class SpellsIntent : MVIIntent {
    object LoadSpellsIntent : SpellsIntent()
}
