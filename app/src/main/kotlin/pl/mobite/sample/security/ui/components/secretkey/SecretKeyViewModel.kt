package pl.mobite.sample.security.ui.components.secretkey

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.utils.SchedulerProvider


class SecretKeyViewModel(
        secretKeyRepository: SecretKeyRepository,
        schedulerProvider: SchedulerProvider,
        initialState: SecretKeyViewState?
): ViewModel() {

    private val secretKeyIntentSource = PublishRelay.create<SecretKeyIntent>()

    private val secretKeyViewStateSource: Observable<SecretKeyViewState> by lazy {
        secretKeyIntentSource
                .map(SecretKeyIntentInterpreter())
                .compose(SecretKeyActionProcessor(secretKeyRepository, schedulerProvider))
                .scan(initialState ?: SecretKeyViewState.default(), SecretKeyReducer())
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    fun processIntents(intents: Observable<SecretKeyIntent>) {
        intents.subscribe(secretKeyIntentSource)
    }

    fun states(): Observable<SecretKeyViewState> {
        return secretKeyViewStateSource
    }
}