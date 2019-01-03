package pl.mobite.sample.security.ui.base

import io.reactivex.Observable


abstract class MviIntent<A: MviAction>(val action: A)

interface MviAction

interface MviProcessor<A: MviAction, R: MviResult<*>> {

    fun process(action: A): Observable<R>
}

interface MviResult<VS: MviViewState> {

    fun reduce(prevState: VS): VS
}

interface MviViewState