package pl.mobite.sample.security.ui.base.mvi

import io.reactivex.Observable


interface MviAction

interface MviProcessor<A: MviAction, R: MviResult> {

    fun process(action: A): Observable<R>
}

interface MviResult

interface MviViewState<R: MviResult> {

    fun reduce(result: R): MviViewState<R>
}
