package pl.mobite.sample.security.ui.base

import io.reactivex.Observable
import io.reactivex.ObservableEmitter


fun <T> ObservableEmitter<T>.onNextSafe(item: T) {
    if (!isDisposed) {
        onNext(item)
    }
}

fun <T> ObservableEmitter<T>.onCompleteSafe() {
    if (!isDisposed) {
        onComplete()
    }
}

fun <T>asObservable(doStuff: ObservableEmitter<T>.() -> Unit): Observable<T> = Observable.create<T> { emitter ->
    emitter.doStuff()
    emitter.onCompleteSafe()
}