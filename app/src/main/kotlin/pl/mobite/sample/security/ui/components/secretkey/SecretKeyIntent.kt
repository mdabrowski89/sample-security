package pl.mobite.sample.security.ui.components.secretkey

import io.reactivex.Observable
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.ui.base.MviAction
import pl.mobite.sample.security.ui.base.MviIntent
import pl.mobite.sample.security.ui.base.MviProcessor
import pl.mobite.sample.security.ui.base.MviResult
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyAction.*


sealed class SecretKeyIntent(action: SecretKeyAction): MviIntent<SecretKeyAction>(action) {

    data class InitialIntent(val keyAlias: String): SecretKeyIntent(CheckKeyAction(keyAlias))

    data class GenerateKeyIntent(val keyAlias: String): SecretKeyIntent(GenerateNewKeyAction(keyAlias))

    data class RemoveKeyIntent(val keyAlias: String): SecretKeyIntent(RemoveKeyAction(keyAlias))

    data class EncryptMessageIntent(val keyAlias: String, val messageToEncrypt: String): SecretKeyIntent(EncryptMessageAction(keyAlias, messageToEncrypt))

    data class DecryptMessageIntent(val keyAlias: String, val messageEncrypted: String): SecretKeyIntent(DecryptMessageAction(keyAlias, messageEncrypted))

    object ClearMessagesIntent: SecretKeyIntent(ClearMessagesAction)
}

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
        return secretKeyRepository.checkKey(action.keyAlias)
            .toObservable()
            .map { hasKey -> if (hasKey) {
                SecretKeyResult.HasValidKeyResult(action.keyAlias)
            } else {
                SecretKeyResult.NoValidKeyResult
            }}
    }

}

class GenerateNewKeyProcessor(
    private val secretKeyRepository: SecretKeyRepository
): MviProcessor<GenerateNewKeyAction, SecretKeyResult> {

    override fun process(action: GenerateNewKeyAction): Observable<SecretKeyResult> {
        return secretKeyRepository.generateKey(action.keyAlias)
            .toSingleDefault(SecretKeyResult.HasValidKeyResult(action.keyAlias))
            .toObservable()
            .cast(SecretKeyResult::class.java)
    }
}

class RemoveKeyProcessor(
    private val secretKeyRepository: SecretKeyRepository
): MviProcessor<RemoveKeyAction, SecretKeyResult> {

    override fun process(action: RemoveKeyAction): Observable<SecretKeyResult> {
        return secretKeyRepository.removeKey(action.keyAlias)
            .toSingleDefault(SecretKeyResult.NoValidKeyResult)
            .toObservable()
            .cast(SecretKeyResult::class.java)
    }
}

class EncryptMessageProcessor(
    private val secretKeyRepository: SecretKeyRepository
): MviProcessor<EncryptMessageAction, SecretKeyResult> {

    override fun process(action: EncryptMessageAction): Observable<SecretKeyResult> {
        return secretKeyRepository.encrypt(action.keyAlias, action.messageToEncrypt)
            .toObservable()
            .map { messageEncrypted -> SecretKeyResult.EncryptMessageResult(action.keyAlias, messageEncrypted) }
            .cast(SecretKeyResult::class.java)
    }
}

class DecryptMessageProcessor(
    private val secretKeyRepository: SecretKeyRepository
): MviProcessor<DecryptMessageAction, SecretKeyResult> {

    override fun process(action: DecryptMessageAction): Observable<SecretKeyResult> {
        return secretKeyRepository.decrypt(action.keyAlias, action.messageToDecrypt)
            .toObservable()
            .map { messageDecrypted ->
                SecretKeyResult.DecryptMessageResult(
                    action.keyAlias,
                    action.messageToDecrypt,
                    messageDecrypted
                )
            }
            .cast(SecretKeyResult::class.java)
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
