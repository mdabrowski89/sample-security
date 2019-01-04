package pl.mobite.sample.security.ui.components.secretkey

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.utils.SchedulerProvider


class SecretKeyViewModel(
        secretKeyRepository: SecretKeyRepository,
        schedulerProvider: SchedulerProvider,
        initialState: SecretKeyViewState?
): ViewModel() {

    private lateinit var disposable: Disposable

    private val actionSource = PublishRelay.create<SecretKeyAction>()

    val states: Observable<SecretKeyViewState> by lazy {
        actionSource
                .compose(SecretKeyActionProcessor(secretKeyRepository, schedulerProvider))
                .scan(initialState ?: SecretKeyViewState.default()) { prevState, result -> result.reduce(prevState) }
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    fun processActions(actions: Observable<SecretKeyAction>) {
        disposable = actions.subscribe(actionSource)
    }

    fun clear() {
        disposable.dispose()
    }
}