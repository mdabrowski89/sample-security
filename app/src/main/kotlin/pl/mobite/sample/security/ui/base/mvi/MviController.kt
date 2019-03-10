package pl.mobite.sample.security.ui.base.mvi

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.CompositeDisposable


class MviController<A: MviAction, R: MviResult, VS: MviViewState<R>>(
    private val viewModelProvider: ViewModelProvider,
    private val viewStateParcelKey: String,
    private val lifecycle: Lifecycle,
    private val callback: MviControllerCallback<A, R, VS>
): LifecycleObserver {

    var viewState: VS? = null

    private lateinit var viewModel: MviViewModel<A, R, VS>

    private val actionsRelay = PublishRelay.create<A>()
    private var disposable = CompositeDisposable()

    fun initViewModel(viewModelClass: Class<out MviViewModel<A, R, VS>>) {
        viewModel = viewModelProvider.get(viewModelClass)
        lifecycle.addObserver(this)
    }

    fun initViewStatesObservable(savedInstanceState: Bundle?) {
        val lastViewState = savedInstanceState?.getParcelable(viewStateParcelKey) as? VS? ?: viewState
        viewModel.initViewStatesObservable(lastViewState, callback.alwaysInitWithSavedViewState())
    }

    fun accept(action: A) {
        actionsRelay.accept(action)
    }

    fun saveLastViewState(outState: Bundle) {
        outState.putParcelable(viewStateParcelKey, callback.updateViewStateBeforeSave(viewState))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        viewModel.getViewStatesObservable().subscribe(this::render).run { disposable.add(this) }
        viewModel.processActions(actionsRelay)

        callback.initialAction(viewState)?.let { action -> accept(action) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        disposable.clear()
        viewModel.clear()
    }

    private fun render(viewState: VS) {
        callback.render(viewState)
        if (viewState.isSavable()) {
            this.viewState = viewState
        }
    }
}

interface MviControllerCallback<A: MviAction, R: MviResult, VS: MviViewState<R>> {

    /**
     * Sends this action right after subscription to viewStates observer
     */
    fun initialAction(lastViewState: VS?): A? = null

    /**
     * Update UI based on ViewState
     */
    fun render(viewState: VS)

    /**
     * Update the last rendered view state before:
     * 1. it will be saved to outBundle in onSaveInstanceState() method
     * 2. the view will be destroyed in onDestroyView() method
     */
    fun updateViewStateBeforeSave(viewState: VS?): VS? = viewState

    /**
     * When fragment view (or activity) is recreated use saved view state instead of re emitting the last one
     */
    fun alwaysInitWithSavedViewState() = false
}