package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.main.MainAction.SearchByQueryAction
import com.epam.harrypotterspells.feature.main.MainResult.SearchByQueryResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<SearchByQueryAction, SearchByQueryResult> {

    override fun performAction() = ObservableTransformer<SearchByQueryAction, SearchByQueryResult> {
        it.flatMap { action ->
            repository.searchByQuery(action.query)
            Observable.just(SearchByQueryResult)
        }
    }

}