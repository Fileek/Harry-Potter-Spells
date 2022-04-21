package com.epam.harrypotterspells.list

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.network.SpellApiImpl
import io.reactivex.rxjava3.core.Observable

class ListViewModel : ViewModel() {
    private val store = Store(
        initialState = ListState.Empty,
        middleware = ListMiddleware(SpellApiImpl),
        reducer = ListReducer()
    )

    val listState: Observable<ListState> = store.state

    init {
        val action = ListAction.Loading
        store.dispatch(action)
    }
}