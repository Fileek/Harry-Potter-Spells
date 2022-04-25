package com.epam.harrypotterspells.list

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.network.Spell
import com.epam.harrypotterspells.network.SpellApiImpl
import io.reactivex.rxjava3.core.Observable

class ListViewModel : ViewModel() {
    private val store = Store(
        initialState = ListState.Empty,
        reducer = ListReducer(),
        middleware = ListMiddleware(SpellApiImpl)
    )

    val listState: Observable<ListState> = store.state

    init {
        val action = ListAction.Loading
        store.dispatch(action)
    }

    fun edit(spell: Spell) {
        val action = ListAction.Edit(spell)
        store.dispatch(action)
    }
}