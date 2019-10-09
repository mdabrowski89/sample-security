package pl.mobite.sample.security.ui.components.pin.mvi

import android.security.keystore.UserNotAuthenticatedException
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.koin.core.KoinComponent
import org.koin.core.inject
import pl.mobite.sample.security.ui.base.mvi.*
import pl.mobite.sample.security.ui.components.pin.mvi.PinAction.*
import pl.mobite.sample.security.ui.components.pin.mvi.PinResult.*
import pl.mobite.sample.security.uscases.*
import pl.mobite.sample.security.utils.hasMarshmallow


class PinActionProcessor: MviActionsProcessor<PinAction, PinResult>(), KoinComponent {

    private val schedulersProvider: SchedulersProvider by inject()
    private val checkIfDeviceIsSecureUseCase: CheckIfDeviceIsSecureUseCase by inject()
    private val getKeyForPinUseCase: GetKeyForPinUseCase by inject()
    private val generateKeyForPinUseCase: GenerateKeyForPinUseCase by inject()
    private val removeSecretKeyUseCase: RemoveSecretKeyUseCase by inject()
    private val encryptWithPinUseCase: EncryptWithPinUseCase by inject()
    private val decryptWithPinUseCase: DecryptWithPinUseCase by inject()

    override fun getActionProcessors(shared: Observable<PinAction>) = listOf(
        shared.connect(checkPreconditionsProcessor),
        shared.connect(generateNewKeyProcessor),
        shared.connect(removeKeyProcessor),
        shared.connect(encryptMessageProcessor),
        shared.connect(decryptMessageProcessor),
        shared.connect(clearMessagesProcessor)
    )

    private val checkPreconditionsProcessor =
        createFingerprintActionProcessor<CheckPreconditionsAndKeyAction> { action ->
            val isMarshmallow = hasMarshmallow()
            val isDeviceSecure = checkIfDeviceIsSecureUseCase()
            val checkPreconditionsResult = CheckPreconditionsResult(
                isMarshmallow = isMarshmallow,
                isDeviceSecure = isDeviceSecure
            )
            onNextSafe(checkPreconditionsResult)
            val result = getKeyForPinUseCase(action.keyAlias)?.let {HasValidKeyResult(it, action.keyAlias)} ?: NoValidKeyResult
            onNextSafe(result)
            onCompleteSafe()
        }

    private val generateNewKeyProcessor =
        createFingerprintActionProcessor<GenerateNewKeyAction> { action ->
            val keyPair = generateKeyForPinUseCase(action.keyAlias)
            onNextSafe(HasValidKeyResult(keyPair, action.keyAlias))
            onCompleteSafe()
        }

    private val removeKeyProcessor =
        createFingerprintActionProcessor<RemoveKeyAction> { action ->
            removeSecretKeyUseCase(action.keyAlias)
            onNextSafe(NoValidKeyResult)
            onCompleteSafe()
        }

    private val encryptMessageProcessor =
        createFingerprintActionProcessor<EncryptMessageAction> { action ->
            val messageEncrypted = encryptWithPinUseCase(action.messageToEncrypt, action.publicKey)
            onNextSafe(EncryptMessageResult(messageEncrypted))
            onCompleteSafe()
        }

    private val decryptMessageProcessor =
        createFingerprintActionProcessor<DecryptMessageAction> { action ->
            try {
                val messageDecrypted = decryptWithPinUseCase(action.messageToDecrypt, action.privateKey)
                onNextSafe(DecryptMessageResult(messageDecrypted))
            } catch (t: Throwable) {
                if (action.authenticateIfNeeded && hasMarshmallow() && t is UserNotAuthenticatedException) {
                    onNextSafe(AuthenticationRequiredResult)
                } else {
                    onNextSafe(ErrorResult(t))
                }
            }
            onCompleteSafe()
        }

    private val clearMessagesProcessor =
        createActionProcessor<ClearMessagesAction, PinResult>(
            schedulersProvider
        ) {
            onNextSafe(ClearMessagesResult)
            onCompleteSafe()
        }

    private fun <A: PinAction>createFingerprintActionProcessor(
        doStuff: ObservableEmitter<PinResult>.(action: A) -> Unit
    ) = createActionProcessor(
        schedulersProvider,
        { InFlightResult },
        { ErrorResult(it) },
        doStuff
    )
}