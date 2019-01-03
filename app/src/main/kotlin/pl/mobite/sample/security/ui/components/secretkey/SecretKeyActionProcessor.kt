package pl.mobite.sample.security.ui.components.secretkey

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.ui.base.MviProcessorRxWrapper
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyAction.*
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyResult.ErrorResult
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyResult.InFlightResult
import pl.mobite.sample.security.utils.SchedulerProvider


class SecretKeyActionProcessor(
        secretKeyRepository: SecretKeyRepository,
        schedulerProvider: SchedulerProvider
): ObservableTransformer<SecretKeyAction, SecretKeyResult> {

    override fun apply(actions: Observable<SecretKeyAction>): ObservableSource<SecretKeyResult> {
        return actions.publish { shared ->
            Observable.merge(listOf(
                    shared.ofType(CheckKeyAction::class.java).compose(checkKeyProcessor),
                    shared.ofType(GenerateNewKeyAction::class.java).compose(generateNewKeyProcessor),
                    shared.ofType(RemoveKeyAction::class.java).compose(removeKeyProcessor),
                    shared.ofType(EncryptMessageAction::class.java).compose(encryptMessageProcessor),
                    shared.ofType(DecryptMessageAction::class.java).compose(decryptMessageProcessor),
                    shared.ofType(ClearMessagesAction::class.java).compose(clearMessagesProcessor)
            ))
        }
    }

    private val checkKeyProcessor = MviProcessorRxWrapper(
        schedulerProvider,
        CheckKeyProcessor(secretKeyRepository),
        InFlightResult
    ) {ErrorResult(it)}

    private val generateNewKeyProcessor = MviProcessorRxWrapper(
        schedulerProvider,
        GenerateNewKeyProcessor(secretKeyRepository),
        InFlightResult
    ) {ErrorResult(it)}

    private val removeKeyProcessor = MviProcessorRxWrapper(
        schedulerProvider,
        RemoveKeyProcessor(secretKeyRepository),
        InFlightResult
    ) {ErrorResult(it)}

    private val encryptMessageProcessor = MviProcessorRxWrapper(
        schedulerProvider,
        EncryptMessageProcessor(secretKeyRepository),
        InFlightResult
    ) {ErrorResult(it)}

    private val decryptMessageProcessor = MviProcessorRxWrapper(
        schedulerProvider,
        DecryptMessageProcessor(secretKeyRepository),
        InFlightResult
    ) {ErrorResult(it)}

    private val clearMessagesProcessor = MviProcessorRxWrapper(
        schedulerProvider,
        ClearMessagesProcessor()
    )
}