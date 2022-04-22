package com.epam.harrypotterspells.list

import com.epam.harrypotterspells.network.SpellApi
import com.epam.harrypotterspells.redux.Middleware
import io.reactivex.rxjava3.schedulers.Schedulers

class ListMiddleware(private val api: SpellApi) : Middleware<ListState, ListAction> {

    override fun process(action: ListAction, store: Store<ListState, ListAction>) {
        if (action is ListAction.Loading) {
            observeApiResponse(store)
        }
    }

    private fun observeApiResponse(store: Store<ListState, ListAction>) {
        api.getSpells()
            .subscribeOn(Schedulers.io())
            .subscribe({
                val newAction = ListAction.Success(it)
                store.dispatch(newAction)
            }, {
                val newAction = ListAction.Failure(it)
                store.dispatch(newAction)
            })
    }
}