package pl.mobite.sample.security.ui.components.pin

import kotlinx.android.parcel.Parcelize
import pl.mobite.sample.security.ui.base.mvi.MviViewState
import pl.mobite.sample.security.ui.base.mvi.ViewStateEmptyEvent
import pl.mobite.sample.security.ui.base.mvi.ViewStateErrorEvent
import pl.mobite.sample.security.ui.components.pin.mvi.PinResult
import pl.mobite.sample.security.ui.components.pin.mvi.PinResult.*
import pl.mobite.sample.security.ui.custom.encryptionform.EncryptionFormViewState
import java.security.KeyPair

@Parcelize
data class PinViewState(
    val isMarshmallow: Boolean,
    val isDeviceSecure: Boolean,
    val encryptionFormViewState: EncryptionFormViewState,
    val messageToEncrypt: String?,
    val keyPair: KeyPair?,
    val errorEvent: ViewStateErrorEvent?,
    val authenticationRequired: ViewStateEmptyEvent?
): MviViewState<PinResult> {

    companion object {

        fun default() = PinViewState(
            isMarshmallow = false,
            isDeviceSecure = false,
            encryptionFormViewState = EncryptionFormViewState.default(),
            messageToEncrypt = null,
            keyPair = null,
            errorEvent = null,
            authenticationRequired = null
        )
    }

    override fun reduce(result: PinResult): PinViewState {
        return when (result) {
            is InFlightResult -> result.reduce()
            is ErrorResult -> result.reduce()
            is CheckPreconditionsResult -> result.reduce()
            is HasValidKeyResult -> result.reduce()
            is NoValidKeyResult -> result.reduce()
            is EncryptMessageResult -> result.reduce()
            is AuthenticationRequiredResult -> result.reduce()
            is DecryptMessageResult -> result.reduce()
            is ClearMessagesResult -> result.reduce()
        }
    }

    override fun isSavable() = !encryptionFormViewState.inProgress

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
        errorEvent = ViewStateErrorEvent(t)
    )

    private fun CheckPreconditionsResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false
        ),
        errorEvent = null,
        isMarshmallow = this.isMarshmallow,
        isDeviceSecure = this.isDeviceSecure
    )
    private fun HasValidKeyResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false,
            keyAlias = keyAlias
        ),
        keyPair = keyPair,
        errorEvent = null
    )

    private fun NoValidKeyResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false,
            keyAlias = null,
            messageEncrypted = null,
            messageDecrypted = null,
            clearEvent = ViewStateEmptyEvent()
        ),
        keyPair = null,
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

    private fun AuthenticationRequiredResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false
        ),
        errorEvent = null,
        authenticationRequired = ViewStateEmptyEvent()
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
}