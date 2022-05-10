package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.mvibase.MVIAction
import com.epam.harrypotterspells.mvibase.MVIResult
import io.reactivex.rxjava3.core.ObservableTransformer

interface UseCase<A: MVIAction, R: MVIResult> {
    fun performAction(): ObservableTransformer<A, R>
}