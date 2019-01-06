package pl.mobite.sample.security.ui.base.mvi

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable


abstract class MviViewModel<A: MviAction, R: MviResult, VS: MviViewState<R>>(
    processor: ObservableTransformer<A, R>,
    initialState: VS
): ViewModel() {

    private lateinit var disposable: Disposable

    private val actionSource = PublishRelay.create<A>()

    @Suppress("UNCHECKED_CAST")
    val states: Observable<VS> by lazy {
        actionSource
            .compose(processor)
            .scan<VS>(initialState) { prevState: VS, result -> prevState.reduce(result) as VS }
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    fun processActions(actions: Observable<A>) {
        disposable = actions.subscribe(actionSource)
    }

    fun clear() {
        disposable.dispose()
    }
}