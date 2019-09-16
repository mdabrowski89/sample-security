package pl.mobite.sample.security.ui.components.fingerprint.mvi

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.koin.core.KoinComponent
import org.koin.core.inject
import pl.mobite.sample.security.ui.base.mvi.*
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintAction.*
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintResult.*
import pl.mobite.sample.security.uscases.*
import pl.mobite.sample.security.utils.hasMarshmallow


class FingerprintActionProcessor: MviActionsProcessor<FingerprintAction, FingerprintResult>(), KoinComponent {

    private val schedulersProvider: SchedulersProvider by inject()
    private val checkFingerprintHardwareUseCase: CheckFingerprintHardwareUseCase by inject()
    private val checkFingerprintEnrolledUseCase: CheckFingerprintEnrolledUseCase by inject()
    private val checkIfDeviceIsSecureUseCase: CheckIfDeviceIsSecureUseCase by inject()
    private val getSecretKeyUseCase: GetSecretKeyUseCase by inject()
    private val generateSecretKeyForFingerprintUseCase: GenerateSecretKeyForFingerprintUseCase by inject()
    private val removeSecretKeyUseCase: RemoveSecretKeyUseCase by inject()
    private val getEncryptionCipherUseCase: GetEncryptionCipherUseCase by inject()
    private val encryptWithFingerprintCipherUseCase: EncryptWithFingerprintCipherUseCase by inject()
    private val getDecryptionCipherUseCase: GetDecryptionCipherUseCase by inject()
    private val decryptWithFingerprintCipherUseCase: DecryptWithFingerprintCipherUseCase by inject()

    override fun getActionProcessors(shared: Observable<FingerprintAction>) = listOf(
        shared.connect(checkPreconditionsProcessor),
        shared.connect(generateNewKeyProcessor),
        shared.connect(removeKeyProcessor),
        shared.connect(prepareEncryptionCipherProcessor),
        shared.connect(encryptMessageProcessor),
        shared.connect(prepareDecryptionCipherProcessor),
        shared.connect(decryptMessageProcessor),
        shared.connect(clearMessagesProcessor)
    )

    private val checkPreconditionsProcessor =
        createFingerprintActionProcessor<CheckPreconditionsAndKeyAction> { action ->
            val isMarshmallow = hasMarshmallow()
            val hasFingerprintScanner = checkFingerprintHardwareUseCase()
            val hasFingerprintEnrolled = checkFingerprintEnrolledUseCase()
            val isDeviceSecure = checkIfDeviceIsSecureUseCase()
            val checkPreconditionsResult = CheckPreconditionsResult(
                isMarshmallow = isMarshmallow,
                hasFingerprintScanner = hasFingerprintScanner,
                hasFingerprintEnrolled = hasFingerprintEnrolled,
                isDeviceSecure = isDeviceSecure
            )
            onNextSafe(checkPreconditionsResult)
            val result = getSecretKeyUseCase(action.keyAlias)?.let {HasValidKeyResult(it, action.keyAlias)} ?: NoValidKeyResult
            onNextSafe(result)
            onCompleteSafe()
        }

    private val generateNewKeyProcessor =
        createFingerprintActionProcessor<GenerateNewKeyAction> { action ->
            val secretKey = generateSecretKeyForFingerprintUseCase(action.keyAlias)
            onNextSafe(HasValidKeyResult(secretKey, action.keyAlias))
            onCompleteSafe()
        }

    private val removeKeyProcessor =
        createFingerprintActionProcessor<RemoveKeyAction> { action ->
            removeSecretKeyUseCase(action.keyAlias)
            onNextSafe(NoValidKeyResult)
            onCompleteSafe()
        }

    private val prepareEncryptionCipherProcessor =
        createFingerprintActionProcessor<PrepareEncryptionCipherAction> { action ->
            onNextSafe(EncryptionCipherReadyResult(getEncryptionCipherUseCase(action.secretKey), action.messageToEncrypt))
            onCompleteSafe()
        }

    private val encryptMessageProcessor =
        createFingerprintActionProcessor<EncryptMessageAction> { action ->
            val messageEncrypted = encryptWithFingerprintCipherUseCase(action.messageToEncrypt, action.authenticatedCipher)
            onNextSafe(EncryptMessageResult(messageEncrypted))
            onCompleteSafe()
        }

    private val prepareDecryptionCipherProcessor =
        createFingerprintActionProcessor<PrepareDecryptionCipherAction> { action ->
            onNextSafe(DecryptionCipherReadyResult(getDecryptionCipherUseCase(action.secretKey)))
            onCompleteSafe()
        }

    private val decryptMessageProcessor =
        createFingerprintActionProcessor<DecryptMessageAction> { action ->
            val messageDecrypted = decryptWithFingerprintCipherUseCase(action.messageToDecrypt, action.authenticatedCipher)
            onNextSafe(DecryptMessageResult(messageDecrypted))
            onCompleteSafe()
        }

    private val clearMessagesProcessor =
        createActionProcessor<ClearMessagesAction, FingerprintResult>(
            schedulersProvider
        ) {
            onNextSafe(ClearMessagesResult)
            onCompleteSafe()
        }

    private fun <A: FingerprintAction>createFingerprintActionProcessor(
        doStuff: ObservableEmitter<FingerprintResult>.(action: A) -> Unit
    ) = createActionProcessor(
        schedulersProvider,
        { InFlightResult },
        { ErrorResult(it) },
        doStuff
    )
}