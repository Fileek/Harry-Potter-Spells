package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.main.MainAction.SearchAction
import com.epam.harrypotterspells.feature.main.MainResult.SearchResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<SearchAction, SearchResult> {

    override fun performAction() = ObservableTransformer<SearchAction, SearchResult> {
        it.flatMap { action ->
            Observable.just(
                when (action) {
                    is SearchAction.OpenAction -> {
                        SearchResult.OpenResult
                    }
                    is SearchAction.QueryAction -> {
                        repository.searchByQuery(action.query)
                        SearchResult.QueryResult
                    }
                    is SearchAction.CloseAction -> {
                        SearchResult.CloseResult
                    }
                }
            )
        }
    }

}