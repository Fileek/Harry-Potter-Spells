package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.feature.spells.SpellsAction.GetRemoteAction
import com.epam.harrypotterspells.feature.spells.SpellsResult.RemoteResult
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

/**
 * UseCase to retrieve data from [RemoteRepository].
 * @param repository remote data source.
 */
class GetRemoteSpellsUseCase @Inject constructor(
    private val repository: RemoteRepository
) : UseCase {

    fun performAction() = ObservableTransformer<GetRemoteAction, RemoteResult> {
        it.flatMap {
            repository.getSpells()
                .map { data ->
                    val spells = data.map { jsonSpell -> jsonSpell.toSpell() }
                    RemoteResult.Success(spells)
                }
                .cast(RemoteResult::class.java)
                .onErrorReturn { error ->
                    RemoteResult.Error(error)
                }
                .toObservable()
                .startWithItem(RemoteResult.Loading)
        }
    }
}