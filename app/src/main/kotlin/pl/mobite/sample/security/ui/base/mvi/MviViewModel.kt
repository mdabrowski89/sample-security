package pl.mobite.sample.security.ui.base.mvi

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable


abstract class MviViewModel<A: MviAction, R: MviResult, VS: MviViewState<R>>(
    private val processor: ObservableTransformer<A, R>,
    private val defaultViewState: VS
): ViewModel() {

    private var disposables: CompositeDisposable = CompositeDisposable()

    private val actionSource = PublishRelay.create<A>()

    var states: Observable<VS>? = null

    @Suppress("UNCHECKED_CAST")
    fun initStates(savedViewState: VS?) {
        if (states == null || useSavedStateOnFragmentRecreation()) {
            disposables.clear()
            states = actionSource
                .compose(processor)
                .scan<VS>(savedViewState ?: defaultViewState) { viewState: VS, result -> viewState.reduce(result) as VS }
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
        }
    }

    fun processActions(actions: Observable<A>) {
        disposables.add(actions.subscribe(actionSource))
    }

    fun clear() {
        disposables.clear()
    }

    /**
     * In ViewModel subclass change to `true` if:
     * - fragment contains views which handle its state recreation by them self
     * When set to true than during fragment recreation we do not subscribe to the previously created observer
     * with views states, but instead we created new one.
     * As a consequence: on fragment recreation we do not re emit the last ViewState but instead the saved one
     * Useful in fragments when we do not want to loose view date which are not stored in the view state
     */
    protected open fun useSavedStateOnFragmentRecreation() = false
}