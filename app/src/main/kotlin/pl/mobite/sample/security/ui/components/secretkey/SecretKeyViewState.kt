package pl.mobite.sample.security.ui.components.secretkey

import kotlinx.android.parcel.Parcelize
import pl.mobite.sample.security.ui.base.mvi.MviViewState
import pl.mobite.sample.security.ui.base.mvi.ViewStateEvent
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult.*

@Parcelize
data class SecretKeyViewState(
    val secretKeyAlias: String?,
    val messageEncrypted: String?,
    val messageDecrypted: String?,
    val isLoading: Boolean,
    val clearEvent: ViewStateEvent<Boolean>,
    val error: ViewStateEvent<Throwable>?
): MviViewState<SecretKeyResult> {

    companion object {
        fun default() = SecretKeyViewState(
            secretKeyAlias = null,
            messageEncrypted = null,
            messageDecrypted = null,
            isLoading = false,
            clearEvent = ViewStateEvent(false),
            error = null
        )
    }

    override fun isSavable() = !isLoading

    override fun reduce(result: SecretKeyResult): SecretKeyViewState {
        return when (result) {
            is HasValidKeyResult -> result.reduce()
            is NoValidKeyResult -> result.reduce()
            is EncryptMessageResult -> result.reduce()
            is DecryptMessageResult -> result.reduce()
            is ClearMessagesResult -> result.reduce()
            is InFlightResult -> result.reduce()
            is ErrorResult -> result.reduce()
        }
    }

    private fun HasValidKeyResult.reduce() = copy(
        isLoading = false,
        secretKeyAlias = keyAlias,
        error = null
    )

    private fun NoValidKeyResult.reduce() = copy(
        isLoading = false,
        secretKeyAlias = null,
        messageEncrypted = null,
        messageDecrypted = null,
        error = null,
        clearEvent = ViewStateEvent(true)
    )

    private fun EncryptMessageResult.reduce() = copy(
        isLoading = false,
        secretKeyAlias = keyAlias,
        messageEncrypted = messageEncrypted,
        messageDecrypted = null,
        error = null
    )

    private fun DecryptMessageResult.reduce() = copy(
        isLoading = false,
        secretKeyAlias = keyAlias,
        messageEncrypted = messageEncrypted,
        messageDecrypted = messageDecrypted,
        error = null
    )

    private fun ClearMessagesResult.reduce() = copy(
        isLoading = false,
        messageEncrypted = null,
        messageDecrypted = null,
        error = null,
        clearEvent = ViewStateEvent(true)
    )

    private fun InFlightResult.reduce() = copy(
        isLoading = true,
        error = null
    )

    private fun ErrorResult.reduce() = copy(
        isLoading = false,
        error = ViewStateEvent(error)
    )
}

