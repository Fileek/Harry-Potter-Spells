package com.epam.harrypotterspells.mvibase

import io.reactivex.rxjava3.core.Observable

/**
 * Represents MVI view.
 */
interface MVIView<I : MVIIntent, S: MVIViewState> {

    /**
     * Provides [I] intents of the view.
     */
    fun getIntents(): Observable<I>

    /**
     * Renders given [state].
     */
    fun render(state: S)
}