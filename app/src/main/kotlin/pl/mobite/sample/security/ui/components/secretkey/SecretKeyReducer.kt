package pl.mobite.sample.security.ui.components.secretkey

import io.reactivex.functions.BiFunction
import pl.mobite.sample.security.data.models.ViewStateError
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyResult.*


class SecretKeyReducer: BiFunction<SecretKeyViewState, SecretKeyResult, SecretKeyViewState> {

    override fun apply(prevState: SecretKeyViewState, result: SecretKeyResult): SecretKeyViewState {
        return when (result) {
            is HasValidKeyResult -> prevState.copy(
                    isLoading = false,
                    secretKeyAlias = result.keyAlias,
                    error = null
            )
            is NoValidKeyResult -> prevState.copy(
                    isLoading = false,
                    secretKeyAlias = null,
                    messageEncrypted = null,
                    messageDecrypted = null,
                    error = null
            ).apply { clearMessage.set(true) }
            is EncryptMessageResult -> prevState.copy(
                    isLoading = false,
                    secretKeyAlias = result.keyAlias,
                    messageEncrypted = result.messageEncrypted,
                    messageDecrypted = null,
                    error = null
            )
            is DecryptMessageResult -> prevState.copy(
                    isLoading = false,
                    secretKeyAlias = result.keyAlias,
                    messageEncrypted = result.messageEncrypted,
                    messageDecrypted = result.messageDecrypted,
                    error = null
            )
            is ClearMessagesResult -> prevState.copy(
                    isLoading = false,
                    messageEncrypted = null,
                    messageDecrypted = null,
                    error = null
            ).apply { clearMessage.set(true) }
            is InFlightResult -> prevState.copy(
                    isLoading = true,
                    error = null
            )
            is ErrorResult -> prevState.copy(
                    isLoading = false,
                    error = ViewStateError(result.error)
            )
        }
    }

}