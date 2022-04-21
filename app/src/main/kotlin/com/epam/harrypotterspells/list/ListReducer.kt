package com.epam.harrypotterspells.list

import com.epam.harrypotterspells.redux.Reducer

class ListReducer : Reducer<ListState, ListAction> {

    override fun reduce(state: ListState, action: ListAction): ListState {
        return when (action) {
            is ListAction.Loading -> ListState.Loading
            is ListAction.Success -> ListState.Success(action.spells)
            is ListAction.Failure -> ListState.Failure(action.error)
        }
    }
}