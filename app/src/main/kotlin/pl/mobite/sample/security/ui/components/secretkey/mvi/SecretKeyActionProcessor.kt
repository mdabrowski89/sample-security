package pl.mobite.sample.security.ui.components.secretkey.mvi

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.ui.base.mvi.MviProcessorImpl
import pl.mobite.sample.security.ui.base.mvi.onNextSafe
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyAction.*
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult.ErrorResult
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult.InFlightResult
import pl.mobite.sample.security.utils.SchedulerProvider


class SecretKeyActionProcessor(
    secretKeyRepository: SecretKeyRepository,
    schedulerProvider: SchedulerProvider
): ObservableTransformer<SecretKeyAction, SecretKeyResult> {

    override fun apply(actions: Observable<SecretKeyAction>): ObservableSource<SecretKeyResult> {
        return actions.publish { shared ->
            Observable.merge(
                listOf(
                    shared.ofType(CheckKeyAction::class.java).compose(checkKeyProcessor),
                    shared.ofType(GenerateNewKeyAction::class.java).compose(generateNewKeyProcessor),
                    shared.ofType(RemoveKeyAction::class.java).compose(removeKeyProcessor),
                    shared.ofType(EncryptMessageAction::class.java).compose(encryptMessageProcessor),
                    shared.ofType(DecryptMessageAction::class.java).compose(decryptMessageProcessor),
                    shared.ofType(ClearMessagesAction::class.java).compose(clearMessagesProcessor)
                )
            )
        }
    }

    private val checkKeyProcessor =
        MviProcessorImpl<CheckKeyAction, SecretKeyResult>(
            schedulerProvider,
            InFlightResult,
            { ErrorResult(it) }
        ) { action ->
            val hasKey = secretKeyRepository.checkKey(action.keyAlias)
            if (hasKey) {
                onNextSafe(SecretKeyResult.HasValidKeyResult(action.keyAlias))
            } else {
                onNextSafe(SecretKeyResult.NoValidKeyResult)
            }
        }

    private val generateNewKeyProcessor =
        MviProcessorImpl<GenerateNewKeyAction, SecretKeyResult>(
            schedulerProvider,
            InFlightResult,
            { ErrorResult(it) }
        ) { action ->
            secretKeyRepository.generateKey(action.keyAlias)
            onNextSafe(SecretKeyResult.HasValidKeyResult(action.keyAlias))
        }

    private val removeKeyProcessor =
        MviProcessorImpl<RemoveKeyAction, SecretKeyResult>(
            schedulerProvider,
            InFlightResult,
            { ErrorResult(it) }
        ) { action ->
            secretKeyRepository.removeKey(action.keyAlias)
            onNextSafe(SecretKeyResult.NoValidKeyResult)
        }

    private val encryptMessageProcessor =
        MviProcessorImpl<EncryptMessageAction, SecretKeyResult>(
            schedulerProvider,
            InFlightResult,
            { ErrorResult(it) }
        ) { action ->
            val messageEncrypted = secretKeyRepository.encrypt(action.keyAlias, action.messageToEncrypt)
            onNextSafe(SecretKeyResult.EncryptMessageResult(action.keyAlias, messageEncrypted))
        }

    private val decryptMessageProcessor =
        MviProcessorImpl<DecryptMessageAction, SecretKeyResult>(
            schedulerProvider,
            InFlightResult,
            { ErrorResult(it) }
        ) { action ->
            val messageDecrypted = secretKeyRepository.decrypt(action.keyAlias, action.messageToDecrypt)
            onNextSafe(
                SecretKeyResult.DecryptMessageResult(
                    action.keyAlias,
                    action.messageToDecrypt,
                    messageDecrypted
                )
            )
        }

    private val clearMessagesProcessor =
        MviProcessorImpl<ClearMessagesAction, SecretKeyResult>(
            schedulerProvider
        ) {
            onNextSafe(SecretKeyResult.ClearMessagesResult)
        }
}