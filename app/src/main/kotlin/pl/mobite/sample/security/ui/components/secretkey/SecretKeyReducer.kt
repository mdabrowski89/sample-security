package pl.mobite.sample.security.ui.components.secretkey

import io.reactivex.functions.BiFunction
import pl.mobite.sample.security.data.models.ViewStateError
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyResult.*


class SecretKeyReducer: BiFunction<SecretKeyViewState, SecretKeyResult, SecretKeyViewState> {

    override fun apply(prevState: SecretKeyViewState, result: SecretKeyResult): SecretKeyViewState {
        return when (result) {
            is HasValidKeyResult -> prevState.withKey(result.keyAlias)
            is NoValidKeyResult -> prevState.withoutKey()
            is EncryptMessageResult -> prevState.withMessageEncrypted(result.keyAlias, result.messageEncrypted)
            is DecryptMessageResult -> prevState.withMessageDecrypted(result.keyAlias, result.messageEncrypted, result.messageDecrypted)
            is ClearMessagesResult -> prevState.withMessageCleared()
            is InFlightResult -> prevState.loading()
            is ErrorResult -> prevState.withError(result.error)
        }
    }
}

fun SecretKeyViewState.withKey(secretKeyAlias: String) = this.copy(
        isLoading = false,
        secretKeyAlias = secretKeyAlias,
        error = null
)

fun SecretKeyViewState.withoutKey() = this.copy(
        isLoading = false,
        secretKeyAlias = null,
        messageEncrypted = null,
        messageDecrypted = null,
        error = null
).apply { clearMessage.set(true) }

fun SecretKeyViewState.withMessageEncrypted(secretKeyAlias: String, messageEncrypted: String) = this.copy(
        isLoading = false,
        secretKeyAlias = secretKeyAlias,
        messageEncrypted = messageEncrypted,
        messageDecrypted = null,
        error = null
)

fun SecretKeyViewState.withMessageDecrypted(secretKeyAlias: String, messageEncrypted: String, messageDecrypted: String) = this.copy(
        isLoading = false,
        secretKeyAlias = secretKeyAlias,
        messageEncrypted = messageEncrypted,
        messageDecrypted = messageDecrypted,
        error = null
)

fun SecretKeyViewState.withMessageCleared() = this.copy(
        isLoading = false,
        messageEncrypted = null,
        messageDecrypted = null,
        error = null
).apply { clearMessage.set(true) }

fun SecretKeyViewState.loading() = this.copy(
        isLoading = true,
        error = null
)

fun SecretKeyViewState.withError(throwable: Throwable) = this.copy(
        isLoading = false,
        error = ViewStateError(throwable)
)