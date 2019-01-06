package pl.mobite.sample.security.ui.base.mvi

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import pl.mobite.sample.security.utils.SchedulerProvider


abstract class MviActionsProcessor<A: MviAction, R: MviResult>: ObservableTransformer<A, R> {

    final override fun apply(actions: Observable<A>): ObservableSource<R> {
        return actions.publish { shared ->
            Observable.merge(
                getActionProcessors(shared)
            )
        }
    }

    abstract fun getActionProcessors(shared: Observable<A>): List<Observable<R>>

    inline fun <reified A>Observable<in A>.connect(processor: ObservableTransformer<A, R>): Observable<R> {
        return ofType(A::class.java).compose(processor)
    }
}

fun <A: MviAction, R: MviResult>createActionProcessor(
    schedulerProvider: SchedulerProvider,
    initialResult: R? = null,
    onErrorResult: ((t: Throwable) -> R)? = null,
    doStuff: ObservableEmitter<R>.(action: A) -> Unit
) = ObservableTransformer<A, R> { actions ->
    var observable = actions
        .switchMap { action -> asObservable<R> { doStuff(action) } }

    if (onErrorResult != null) {
        observable = observable.onErrorReturn { t -> onErrorResult.invoke(t) }
    }

    observable = observable
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())

    if (initialResult != null) {
        observable = observable.startWith(initialResult)
    }

    observable
}

fun <T> asObservable(doStuff: ObservableEmitter<T>.() -> Unit): Observable<T> = Observable.create<T> { emitter ->
    emitter.doStuff()
    emitter.onCompleteSafe()
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