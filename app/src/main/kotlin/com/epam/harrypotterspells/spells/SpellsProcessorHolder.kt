package com.epam.harrypotterspells.spells

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.mvibase.MVIProcessorHolder
import com.epam.harrypotterspells.spells.SpellsAction.LoadSpellsAction
import com.epam.harrypotterspells.spells.SpellsResult.LoadSpellsResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SpellsProcessorHolder @Inject constructor(
    repository: Repository
) : MVIProcessorHolder<SpellsAction, SpellsResult> {

    override val actionProcessor = ObservableTransformer<SpellsAction, SpellsResult> { actions ->
        actions.ofType(LoadSpellsAction::class.java).compose(
            loadSpellsProcessor
        )
    }

    private val loadSpellsProcessor =
        ObservableTransformer<LoadSpellsAction, LoadSpellsResult> {
            it.flatMap {
                repository.spells
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { spells ->
                        LoadSpellsResult.Success(spells)
                    }
                    .cast(LoadSpellsResult::class.java)
                    .onErrorReturn { error ->
                        LoadSpellsResult.Error(error)
                    }
                    .startWithItem(LoadSpellsResult.Loading)
            }
        }
}