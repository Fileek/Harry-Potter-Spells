package com.epam.harrypotterspells.util.`typealias`

import com.epam.harrypotterspells.feature.details.DetailsAction
import com.epam.harrypotterspells.feature.details.DetailsResult
import com.epam.harrypotterspells.feature.details.DetailsViewState
import com.epam.harrypotterspells.feature.spells.SpellsAction
import com.epam.harrypotterspells.feature.spells.SpellsResult
import com.epam.harrypotterspells.feature.main.MainAction
import com.epam.harrypotterspells.feature.main.MainResult
import com.epam.harrypotterspells.feature.main.MainViewState
import com.epam.harrypotterspells.feature.spells.SpellsViewState
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

typealias MainReducer =
        BiFunction<MainViewState, MainResult, MainViewState>

typealias SpellsReducer =
        BiFunction<SpellsViewState, SpellsResult, SpellsViewState>

typealias DetailsReducer =
        BiFunction<DetailsViewState, DetailsResult, DetailsViewState>

typealias MainActionTransformer =
        ObservableTransformer<MainAction, MainResult>

typealias DetailsActionTransformer =
        ObservableTransformer<DetailsAction, DetailsResult>

typealias LoadActionTransformer =
        ObservableTransformer<SpellsAction.LoadAction, SpellsResult.LoadResult>

typealias SearchActionTransformer =
        ObservableTransformer<MainAction.SearchAction, MainResult.SearchResult>

typealias SwitchSourceActionTransformer =
        ObservableTransformer<MainAction.SwitchSourceAction, MainResult.SwitchSourceResult>

typealias EditActionTransformer =
        ObservableTransformer<DetailsAction.EditAction, DetailsResult.EditResult>

typealias UpdateActionTransformer =
        ObservableTransformer<DetailsAction.UpdateAction, DetailsResult.UpdateResult>
