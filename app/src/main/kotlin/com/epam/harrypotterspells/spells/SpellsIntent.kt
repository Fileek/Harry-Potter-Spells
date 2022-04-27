package com.epam.harrypotterspells.spells

import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class SpellsIntent : MVIIntent {
    object LoadSpellsIntent : SpellsIntent()
}
