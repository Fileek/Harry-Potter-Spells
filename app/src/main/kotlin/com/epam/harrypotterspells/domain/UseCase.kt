package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.mvibase.MVIAction
import com.epam.harrypotterspells.mvibase.MVIResult
import io.reactivex.rxjava3.core.ObservableTransformer

/**
 * UseCase provides [ObservableTransformer] which transforms action [A] in result [R].
 */
interface UseCase<A: MVIAction, R: MVIResult> {


    /**
     * Performs given action [A] and returns result of type [R].
     */
    fun performAction(): ObservableTransformer<A, R>
}