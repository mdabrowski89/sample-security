package pl.mobite.sample.security.ui.components.secretkey

import kotlinx.android.parcel.Parcelize
import pl.mobite.sample.security.ui.base.mvi.MviViewState
import pl.mobite.sample.security.ui.base.mvi.ViewStateEmptyEvent
import pl.mobite.sample.security.ui.base.mvi.ViewStateErrorEvent
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult.*
import pl.mobite.sample.security.ui.custom.encryptionform.EncryptionFormViewState
import javax.crypto.SecretKey

@Parcelize
data class SecretKeyViewState(
    val encryptionFormViewState: EncryptionFormViewState,
    val secretKey: SecretKey?,
    val errorEvent: ViewStateErrorEvent?
): MviViewState<SecretKeyResult> {

    companion object {
        fun default() = SecretKeyViewState(
            encryptionFormViewState = EncryptionFormViewState.default(),
            secretKey = null,
            errorEvent = null
        )
    }

    override fun isSavable() = !encryptionFormViewState.inProgress

    override fun reduce(result: SecretKeyResult): SecretKeyViewState {
        return when (result) {
            is HasKeyResult -> result.reduce()
            is NoKeyResult -> result.reduce()
            is EncryptMessageResult -> result.reduce()
            is DecryptMessageResult -> result.reduce()
            is ClearMessagesResult -> result.reduce()
            is InFlightResult -> result.reduce()
            is ErrorResult -> result.reduce()
        }
    }

    private fun HasKeyResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false,
            keyAlias = keyAlias
        ),
        secretKey = secretKey,
        errorEvent = null
    )

    private fun NoKeyResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false,
            keyAlias = null,
            messageEncrypted = null,
            messageDecrypted = null,
            clearEvent = ViewStateEmptyEvent()
        ),
        secretKey = null,
        errorEvent = null
    )

    private fun EncryptMessageResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false,
            messageEncrypted = messageEncrypted,
            messageDecrypted = null
        ),
        errorEvent = null
    )

    private fun DecryptMessageResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false,
            messageDecrypted = messageDecrypted
        ),
        errorEvent = null
    )

    private fun ClearMessagesResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false,
            messageEncrypted = null,
            messageDecrypted = null,
            clearEvent = ViewStateEmptyEvent()
        ),
        errorEvent = null
    )

    private fun InFlightResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = true
        ),
        errorEvent = null
    )

    private fun ErrorResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false
        ),
        errorEvent = ViewStateErrorEvent(error)
    )
}

