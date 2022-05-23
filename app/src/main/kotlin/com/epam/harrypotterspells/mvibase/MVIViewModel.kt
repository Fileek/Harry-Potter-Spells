package com.epam.harrypotterspells.mvibase

import io.reactivex.rxjava3.core.Observable

/**
 * Process [MVIIntent]s of given type [I] and provides [MVIViewState]s of type [S].
 */
interface MVIViewModel<I : MVIIntent, S : MVIViewState> {

    /**
     * Process given [observable] of [I] intents.
     */
    fun processIntents(observable: Observable<I>)

    /**
     * Provides view states of type [S].
     */
    fun getStates(): Observable<S>
}