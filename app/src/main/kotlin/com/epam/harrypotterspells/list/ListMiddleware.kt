package com.epam.harrypotterspells.list

import com.epam.harrypotterspells.network.Spell
import com.epam.harrypotterspells.network.SpellApi
import com.epam.harrypotterspells.redux.Middleware
import io.reactivex.rxjava3.schedulers.Schedulers

class ListMiddleware(
    private val api: SpellApi,
) : Middleware<ListState, ListAction> {

    private var _store: Store<ListState, ListAction>? = null

    override fun process(action: ListAction, store: Store<ListState, ListAction>) {
        if (action is ListAction.Loading) {
            _store = store
            observeApiResponse()
        }
    }

    private fun observeApiResponse() {
        api.getSpells()
            .subscribeOn(Schedulers.io())
            .subscribe({
                processSuccessResponse(it)
            }, {
                processFailureResponse(it)
            })
    }

    private fun processSuccessResponse(list: List<Spell>) {
        val newAction = ListAction.Success(list)
        _store?.dispatch(newAction)
    }

    private fun processFailureResponse(throwable: Throwable) {
        val newAction = ListAction.Failure(throwable)
        _store?.dispatch(newAction)
    }
}