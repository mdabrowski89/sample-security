package pl.mobite.sample.security.ui.base.mvi

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.CompositeDisposable


class MviFragmentController<A: MviAction, R: MviResult, VS: MviViewState<R>>(
    private val fragment: Fragment,
    private val render: (VS) -> Unit,
    private val initialAction: ((VS?) -> A?)? = null
): LifecycleObserver {

    var viewState: VS? = null

    private lateinit var viewModel: MviViewModel<A, R, VS>

    private val actionsRelay = PublishRelay.create<A>()
    private var disposable = CompositeDisposable()

    private val viewStateParcelKey = fragment.javaClass.name

    fun onCreate(
        savedInstanceState: Bundle?,
        viewModelFactory: MviViewModelFactory,
        viewModelClass: Class<out MviViewModel<A, R, VS>>
    ) {
        val savedViewState = savedInstanceState?.getParcelable(viewStateParcelKey) as? VS?
        viewModel = ViewModelProviders.of(fragment, viewModelFactory.withArgs(savedViewState)).get(viewModelClass)
        fragment.lifecycle.addObserver(this)
    }

    fun accept(action: A) {
        actionsRelay.accept(action)
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(viewStateParcelKey, viewState)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        disposable.add(viewModel.states.subscribe(this::render))
        viewModel.processActions(actionsRelay)

        initialAction?.invoke(viewState)?.let { action -> accept(action) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        disposable.clear()
        viewModel.clear()
    }

    private fun render(newViewState: VS) {
        render.invoke(newViewState)
        if (newViewState.isSavable()) {
            this.viewState = newViewState
        }
    }
}