package pl.mobite.sample.security.ui.base.mvi

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.CompositeDisposable
import pl.mobite.sample.security.utils.SampleSecurityViewModelFactory


class MviFragmentDelegate<A: MviAction, R: MviResult, VS: MviViewState<R>>(
    private val fragment: Fragment,
    private val render: (VS) -> Unit
): LifecycleObserver {

    private lateinit var viewModel: MviViewModel<A, R, VS>

    private val actionsRelay = PublishRelay.create<A>()
    private var lastViewState: VS? = null
    private var disposable = CompositeDisposable()

    private val parcelKey = fragment.javaClass.name

    fun onCreate(savedInstanceState: Bundle?, modelClass: Class<out MviViewModel<A, R, VS>>) {
        viewModel = ViewModelProviders.of(
            fragment,
            SampleSecurityViewModelFactory.getInstance(savedInstanceState?.getParcelable(parcelKey))
        ).get(modelClass)

        fragment.lifecycle.addObserver(this)
    }

    fun accept(action: A) {
        actionsRelay.accept(action)
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(parcelKey, lastViewState)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        disposable.add(viewModel.states.subscribe(this::render))
        viewModel.processActions(actionsRelay)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        disposable.clear()
        viewModel.clear()
    }

    private fun render(viewState: VS) {
        render.invoke(viewState)
        if (viewState.isSavable()) {
            lastViewState = viewState
        }
    }
}