package pl.mobite.sample.security.ui.base

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import pl.mobite.sample.security.utils.SchedulerProvider


class MviProcessorRxWrapper<A: MviAction, R: MviResult<*>>(
    private val schedulerProvider: SchedulerProvider,
    private val processor: MviProcessor<A, R>,
    private val initialResult: R? = null,
    private val onErrorResult: ((t: Throwable) -> R)? = null
): ObservableTransformer<A, R> {


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