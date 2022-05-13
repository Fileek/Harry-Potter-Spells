package com.epam.harrypotterspells.mvibase

import io.reactivex.rxjava3.core.Observable

interface MVIView<I : MVIIntent, S: MVIViewState> {

    fun getIntents(): Observable<I>

    fun render(state: S)
}