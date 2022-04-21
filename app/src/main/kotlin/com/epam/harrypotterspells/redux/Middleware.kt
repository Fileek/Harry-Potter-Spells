package com.epam.harrypotterspells.redux

import com.epam.harrypotterspells.list.Store

interface Middleware<S : State, A : Action> {
    fun process(store: Store<S, A>)
}