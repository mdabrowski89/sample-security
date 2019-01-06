package pl.mobite.sample.security.ui.base.mvi

import android.os.Parcelable
import io.reactivex.Observable


interface MviAction

interface MviProcessor<A: MviAction, R: MviResult> {

    fun process(action: A): Observable<R>
}

interface MviResult

interface MviViewState<R: MviResult>: Parcelable {

    fun reduce(result: R): MviViewState<R>

    fun isSavable(): Boolean
}
