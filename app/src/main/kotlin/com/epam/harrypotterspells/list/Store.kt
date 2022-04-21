package com.epam.harrypotterspells.list

import android.util.Log
import com.epam.harrypotterspells.redux.Action
import com.epam.harrypotterspells.redux.Middleware
import com.epam.harrypotterspells.redux.Reducer
import com.epam.harrypotterspells.redux.State
import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable

class Store<S : State, A : Action>(
    private val initialState: S,
    private val middleware: Middleware<S, A>,
    private val reducer: Reducer<S, A>
) {
    private val _state: BehaviorRelay<S> = BehaviorRelay.createDefault(initialState)
    val state: Observable<S> = _state.serialize()

    fun dispatch(action: A) {
        Log.v(TAG, "dispatch ${action.javaClass}")
        val newState = reducer.reduce(_state.value ?: initialState, action)
        _state.accept(newState)
        if (action is ListAction.Loading) {
            middleware.process(this)
        }
    }
}