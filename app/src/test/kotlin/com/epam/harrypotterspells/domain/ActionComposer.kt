package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.mvibase.MVIAction
import com.epam.harrypotterspells.mvibase.MVIResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver

class ActionComposer<A : MVIAction, R : MVIResult>(
    private val useCase: UseCase<A, R>
) {

    operator fun invoke(action: A): TestObserver<R> {
        return Observable.just(action)
            .compose(useCase.performAction())
            .test()
    }
}
