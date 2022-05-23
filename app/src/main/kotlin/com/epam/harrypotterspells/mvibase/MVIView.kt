package com.epam.harrypotterspells.mvibase

import io.reactivex.rxjava3.core.Observable

/**
 * View that provides [MVIIntent]s of type [I] and render [MVIViewState] of type [S].
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