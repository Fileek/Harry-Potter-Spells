package com.epam.harrypotterspells.mvibase

import io.reactivex.rxjava3.core.Observable

interface MVIViewModel<I : MVIIntent, S : MVIViewState> {

    fun processIntents(observable: Observable<I>)

    fun states(): Observable<S>
}