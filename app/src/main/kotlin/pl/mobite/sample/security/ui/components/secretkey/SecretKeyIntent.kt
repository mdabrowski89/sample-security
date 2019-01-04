package pl.mobite.sample.security.ui.components.secretkey

import io.reactivex.Observable
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.ui.base.*
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyAction.*

sealed class SecretKeyAction: MviAction {

    data class CheckKeyAction(val keyAlias: String): SecretKeyAction()

    data class GenerateNewKeyAction(val keyAlias: String): SecretKeyAction()

    data class RemoveKeyAction(val keyAlias: String): SecretKeyAction()

    data class EncryptMessageAction(val keyAlias: String, val messageToEncrypt: String): SecretKeyAction()

    data class DecryptMessageAction(val keyAlias: String, val messageToDecrypt: String): SecretKeyAction()

    object ClearMessagesAction: SecretKeyAction()
}

class CheckKeyProcessor(
    private val secretKeyRepository: SecretKeyRepository
): MviProcessor<CheckKeyAction, SecretKeyResult> {

    override fun process(action: CheckKeyAction): Observable<SecretKeyResult> {
        return Observable
            .create<SecretKeyResult> { emitter ->
                val hasKey = secretKeyRepository.checkKey(action.keyAlias)
                if (hasKey) {
                    emitter.onNextSafe(SecretKeyResult.HasValidKeyResult(action.keyAlias))
                } else {
                    emitter.onNextSafe(SecretKeyResult.NoValidKeyResult)
                }
                emitter.onCompleteSafe()
            }
    }
}

class GenerateNewKeyProcessor(
    private val secretKeyRepository: SecretKeyRepository
): MviProcessor<GenerateNewKeyAction, SecretKeyResult> {

    override fun process(action: GenerateNewKeyAction): Observable<SecretKeyResult> {
        return asObservable {
            secretKeyRepository.generateKey(action.keyAlias)
            onNextSafe(SecretKeyResult.HasValidKeyResult(action.keyAlias))
        }
    }
}

class RemoveKeyProcessor(
    private val secretKeyRepository: SecretKeyRepository
): MviProcessor<RemoveKeyAction, SecretKeyResult> {

    override fun process(action: RemoveKeyAction): Observable<SecretKeyResult> {
        return asObservable {
            secretKeyRepository.removeKey(action.keyAlias)
            onNextSafe(SecretKeyResult.NoValidKeyResult)
        }
    }
}

class EncryptMessageProcessor(
    private val secretKeyRepository: SecretKeyRepository
): MviProcessor<EncryptMessageAction, SecretKeyResult> {

    override fun process(action: EncryptMessageAction): Observable<SecretKeyResult> {
        return asObservable {
            val messageEncrypted = secretKeyRepository.encrypt(action.keyAlias, action.messageToEncrypt)
            onNextSafe(SecretKeyResult.EncryptMessageResult(action.keyAlias, messageEncrypted))
        }
    }
}

class DecryptMessageProcessor(
    private val secretKeyRepository: SecretKeyRepository
): MviProcessor<DecryptMessageAction, SecretKeyResult> {

    override fun process(action: DecryptMessageAction): Observable<SecretKeyResult> {
        return asObservable {
            val messageDecrypted = secretKeyRepository.decrypt(action.keyAlias, action.messageToDecrypt)
            onNextSafe(
                SecretKeyResult.DecryptMessageResult(
                    action.keyAlias,
                    action.messageToDecrypt,
                    messageDecrypted
                )
            )
        }
    }
}

class ClearMessagesProcessor: MviProcessor<ClearMessagesAction, SecretKeyResult> {

    override fun process(action: ClearMessagesAction): Observable<SecretKeyResult> {
        return Observable.just(SecretKeyResult.ClearMessagesResult)
    }
}

sealed class SecretKeyResult: MviResult<SecretKeyViewState> {

    data class HasValidKeyResult(val keyAlias: String): SecretKeyResult() {
        override fun reduce(prevState: SecretKeyViewState) = prevState.withKey(keyAlias)
    }

    object NoValidKeyResult: SecretKeyResult() {
        override fun reduce(prevState: SecretKeyViewState) = prevState.withoutKey()
    }

    data class EncryptMessageResult(val keyAlias: String, val messageEncrypted: String): SecretKeyResult() {
        override fun reduce(prevState: SecretKeyViewState) = prevState.withMessageEncrypted(keyAlias, messageEncrypted)
    }

    data class DecryptMessageResult(val keyAlias: String, val messageEncrypted: String, val messageDecrypted: String): SecretKeyResult() {
        override fun reduce(prevState: SecretKeyViewState) = prevState.withMessageDecrypted(keyAlias, messageEncrypted, messageDecrypted)
    }

    object ClearMessagesResult: SecretKeyResult() {
        override fun reduce(prevState: SecretKeyViewState) = prevState.withMessageCleared()
    }

    object InFlightResult: SecretKeyResult() {
        override fun reduce(prevState: SecretKeyViewState) = prevState.loading()
    }

    data class ErrorResult(val error: Throwable): SecretKeyResult() {
        override fun reduce(prevState: SecretKeyViewState) = prevState.withError(error)
    }
}
