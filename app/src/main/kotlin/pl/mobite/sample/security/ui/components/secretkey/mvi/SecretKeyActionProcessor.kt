package pl.mobite.sample.security.ui.components.secretkey.mvi

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.koin.core.KoinComponent
import org.koin.core.inject
import pl.mobite.sample.security.ui.base.mvi.*
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyAction.*
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult.*
import pl.mobite.sample.security.uscases.*


class SecretKeyActionProcessor: MviActionsProcessor<SecretKeyAction, SecretKeyResult>(), KoinComponent {

    private val schedulersProvider: SchedulersProvider by inject()
    private val getSecretKeyUseCase: GetSecretKeyUseCase by inject()
    private val generateSecretKeyUseCase: GenerateSecretKeyUseCase by inject()
    private val removeSecretKeyUseCase: RemoveSecretKeyUseCase by inject()
    private val encryptUseCase: EncryptUseCase by inject()
    private val decryptUseCase: DecryptUseCase by inject()

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
            val hasKey = getSecretKeyUseCase(action.keyAlias) != null
            onNextSafe(if (hasKey) {
                HasValidKeyResult(action.keyAlias)
            } else {
                NoValidKeyResult
            })
            onCompleteSafe()
        }

    private val generateNewKeyProcessor =
        createSecretKeyActionProcessor<GenerateNewKeyAction> { action ->
            generateSecretKeyUseCase(action.keyAlias)
            onNextSafe(HasValidKeyResult(action.keyAlias))
            onCompleteSafe()
        }

    private val removeKeyProcessor =
        createSecretKeyActionProcessor<RemoveKeyAction> { action ->
            removeSecretKeyUseCase(action.keyAlias)
            onNextSafe(NoValidKeyResult)
            onCompleteSafe()
        }

    private val encryptMessageProcessor =
        createSecretKeyActionProcessor<EncryptMessageAction> { action ->
            val secretKey = getSecretKeyUseCase(action.keyAlias)
            if (secretKey != null) {
                val messageEncrypted = encryptUseCase(action.messageToEncrypt, secretKey)
                onNextSafe(EncryptMessageResult(action.keyAlias, messageEncrypted))
            } else {
                throw Exception("Secret key not generated for alias: ${action.keyAlias}")
            }
            onCompleteSafe()
        }

    private val decryptMessageProcessor =
        createSecretKeyActionProcessor<DecryptMessageAction> { action ->
            val secretKey = getSecretKeyUseCase(action.keyAlias)
            if (secretKey != null) {
                val messageDecrypted = decryptUseCase(action.messageToDecrypt, secretKey)
                onNextSafe(
                    DecryptMessageResult(action.keyAlias, action.messageToDecrypt, messageDecrypted)
                )
            } else {
                throw Exception("Secret key not generated for alias: ${action.keyAlias}")
            }
            onCompleteSafe()
        }

    private val clearMessagesProcessor =
        createActionProcessor<ClearMessagesAction, SecretKeyResult>(
            schedulersProvider
        ) {
            onNextSafe(ClearMessagesResult)
            onCompleteSafe()
        }

    private fun <A: SecretKeyAction>createSecretKeyActionProcessor(
        doStuff: ObservableEmitter<SecretKeyResult>.(action: A) -> Unit
    ) = createActionProcessor(
        schedulersProvider,
        { InFlightResult },
        { ErrorResult(it) },
        doStuff
    )
}