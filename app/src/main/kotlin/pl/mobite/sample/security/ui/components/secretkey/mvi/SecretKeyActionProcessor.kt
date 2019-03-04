package pl.mobite.sample.security.ui.components.secretkey.mvi

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.ui.base.mvi.MviActionsProcessor
import pl.mobite.sample.security.ui.base.mvi.SchedulerProvider
import pl.mobite.sample.security.ui.base.mvi.createActionProcessor
import pl.mobite.sample.security.ui.base.mvi.onNextSafe
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyAction.*
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult.*


class SecretKeyActionProcessor: MviActionsProcessor<SecretKeyAction, SecretKeyResult>(), KoinComponent {

    private val secretKeyRepository: SecretKeyRepository by inject()
    private val schedulerProvider: SchedulerProvider by inject()

    override fun getActionProcessors(shared: Observable<SecretKeyAction>) = listOf(
        shared.connect(checkKeyProcessor),
        shared.connect(generateNewKeyProcessor),
        shared.connect(removeKeyProcessor),
        shared.connect(encryptMessageProcessor),
        shared.connect(decryptMessageProcessor),
        shared.connect(clearMessagesProcessor)
    )

    private val checkKeyProcessor =
        createSecretKeyActionProcessor<CheckKeyAction> { action ->
            onNextSafe(if (secretKeyRepository.checkKey(action.keyAlias)) {
                HasValidKeyResult(action.keyAlias)
            } else {
                NoValidKeyResult
            })
        }

    private val generateNewKeyProcessor =
        createSecretKeyActionProcessor<GenerateNewKeyAction> { action ->
            secretKeyRepository.generateKey(action.keyAlias)
            onNextSafe(HasValidKeyResult(action.keyAlias))
        }

    private val removeKeyProcessor =
        createSecretKeyActionProcessor<RemoveKeyAction> { action ->
            secretKeyRepository.removeKey(action.keyAlias)
            onNextSafe(NoValidKeyResult)
        }

    private val encryptMessageProcessor =
        createSecretKeyActionProcessor<EncryptMessageAction> { action ->
            val messageEncrypted = secretKeyRepository.encrypt(action.keyAlias, action.messageToEncrypt)
            onNextSafe(EncryptMessageResult(action.keyAlias, messageEncrypted))
        }

    private val decryptMessageProcessor =
        createSecretKeyActionProcessor<DecryptMessageAction> { action ->
            val messageDecrypted = secretKeyRepository.decrypt(action.keyAlias, action.messageToDecrypt)
            onNextSafe(
                DecryptMessageResult(
                    action.keyAlias,
                    action.messageToDecrypt,
                    messageDecrypted
                )
            )
        }

    private val clearMessagesProcessor =
        createActionProcessor<ClearMessagesAction, SecretKeyResult>(
            schedulerProvider
        ) {
            onNextSafe(ClearMessagesResult)
        }

    private fun <A: SecretKeyAction>createSecretKeyActionProcessor(
        doStuff: ObservableEmitter<SecretKeyResult>.(action: A) -> Unit
    ) = createActionProcessor(
        schedulerProvider,
        { InFlightResult },
        { ErrorResult(it) },
        doStuff
    )
}