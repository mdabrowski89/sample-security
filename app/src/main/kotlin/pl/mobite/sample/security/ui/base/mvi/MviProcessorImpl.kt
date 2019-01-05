package pl.mobite.sample.security.ui.base.mvi

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import pl.mobite.sample.security.utils.SchedulerProvider


class MviProcessorImpl<A: MviAction, R: MviResult>(
    private val schedulerProvider: SchedulerProvider,
    private val initialResult: R? = null,
    private val onErrorResult: ((t: Throwable) -> R)? = null,
    doStuff: ObservableEmitter<R>.(action: A) -> Unit
): ObservableTransformer<A, R> {

    private val processor = createMviProcessor(doStuff)

    override fun apply(actions: Observable<A>): ObservableSource<R> {
        var observable = actions
            .switchMap { action -> processor.process(action) }

        if (onErrorResult != null) {
            observable = observable.onErrorReturn { t -> onErrorResult.invoke(t) }
        }

        observable = observable
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())

        if (initialResult != null) {
            observable = observable.startWith(initialResult)
        }

        return observable
    }
}

fun <A: MviAction, R: MviResult>createMviProcessor(
    doStuff: ObservableEmitter<R>.(action: A) -> Unit
): MviProcessor<A, R> {
    return object: MviProcessor<A, R> {
        override fun process(action: A): Observable<R> {
            return asObservable {
                doStuff(action)
            }
        }
    }
}

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