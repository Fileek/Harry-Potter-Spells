package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.main.MainAction.SearchAction
import com.epam.harrypotterspells.feature.main.MainResult.SearchResult
import com.epam.harrypotterspells.util.`typealias`.SearchActionTransformer
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<SearchAction, SearchResult> {

    override fun performAction() = SearchActionTransformer {
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