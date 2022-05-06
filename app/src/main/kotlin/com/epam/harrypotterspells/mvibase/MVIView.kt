package com.epam.harrypotterspells.mvibase

import io.reactivex.rxjava3.core.Observable

interface MVIView<I : MVIIntent, S: MVIViewState> {

    fun render(state: S)

    fun getIntents(): Observable<I>
}