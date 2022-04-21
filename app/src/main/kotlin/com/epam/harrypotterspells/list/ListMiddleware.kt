package com.epam.harrypotterspells.list

import com.epam.harrypotterspells.network.SpellApi
import com.epam.harrypotterspells.redux.Middleware
import io.reactivex.rxjava3.schedulers.Schedulers

class ListMiddleware(private val api: SpellApi) : Middleware<ListState, ListAction> {

    override fun process(store: Store<ListState, ListAction>) {
        api.getSpells()
            .subscribeOn(Schedulers.io())
            .subscribe({
                val action = ListAction.Success(it)
                store.dispatch(action)
            }, {
                val action = ListAction.Failure(it)
                store.dispatch(action)
            })
    }
}