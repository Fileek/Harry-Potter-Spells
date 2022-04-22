package com.epam.harrypotterspells.redux

import com.epam.harrypotterspells.list.Store

interface Middleware<S : State, A : Action> {
    fun process(action: A, store: Store<S, A>)
}