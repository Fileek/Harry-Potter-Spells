package com.epam.harrypotterspells.mvibase

import io.reactivex.rxjava3.core.ObservableTransformer

interface MVIProcessorHolder<A : MVIAction, R : MVIResult> {

    val actionProcessor: ObservableTransformer<A, R>
}