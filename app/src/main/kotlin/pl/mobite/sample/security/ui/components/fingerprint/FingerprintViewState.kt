package pl.mobite.sample.security.ui.components.fingerprint

import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import pl.mobite.sample.security.ui.base.mvi.MviViewState
import pl.mobite.sample.security.ui.base.mvi.ViewStateEmptyEvent
import pl.mobite.sample.security.ui.base.mvi.ViewStateErrorEvent
import pl.mobite.sample.security.ui.base.mvi.ViewStateNonParcelableEvent
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintResult
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintResult.*
import pl.mobite.sample.security.ui.custom.encryptionform.EncryptionFormViewState
import javax.crypto.Cipher
import javax.crypto.SecretKey

@Parcelize
data class FingerprintViewState(
    val isMarshmallow: Boolean,
    val isHardwareAvailable: Boolean,
    val hasFingerprintEnrolled: Boolean,
    val isDeviceSecure: Boolean,
    val encryptionFormViewState: EncryptionFormViewState,
    val messageToEncrypt: String?,
    val secretKey: SecretKey?,
    val errorEvent: ViewStateErrorEvent?
): MviViewState<FingerprintResult> {

    @IgnoredOnParcel
    var encryptionCipherReadyEvent: ViewStateNonParcelableEvent<Cipher>? = null
    @IgnoredOnParcel
    var decryptionCipherReadyEvent: ViewStateNonParcelableEvent<Cipher>? = null

    companion object {

        fun default() = FingerprintViewState(
            isMarshmallow = false,
            isHardwareAvailable = false,
            hasFingerprintEnrolled = false,
            isDeviceSecure = false,
            encryptionFormViewState = EncryptionFormViewState.default(),
            messageToEncrypt = null,
            secretKey = null,
            errorEvent = null
        )
    }

    override fun reduce(result: FingerprintResult): FingerprintViewState {
        return when (result) {
            is InFlightResult -> result.reduce()
            is ErrorResult -> result.reduce()
            is CheckPreconditionsResult -> result.reduce()
            is HasValidKeyResult -> result.reduce()
            is NoValidKeyResult -> result.reduce()
            is EncryptionCipherReadyResult -> result.reduce()
            is EncryptMessageResult -> result.reduce()
            is DecryptionCipherReadyResult -> result.reduce()
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
        isHardwareAvailable = this.hasFingerprintScanner,
        hasFingerprintEnrolled = this.hasFingerprintEnrolled,
        isDeviceSecure = this.isDeviceSecure
    )
    private fun HasValidKeyResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false,
            keyAlias = keyAlias
        ),
        secretKey = secretKey,
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
        secretKey = null,
        errorEvent = null
    )

    private fun EncryptionCipherReadyResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false,
            messageDecrypted = null
        ),
        messageToEncrypt = messageToEncrypt,
        errorEvent = null
    ).apply {
        encryptionCipherReadyEvent = ViewStateNonParcelableEvent(encryptionCipher)
    }

    private fun EncryptMessageResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false,
            messageEncrypted = messageEncrypted,
            messageDecrypted = null
        ),
        errorEvent = null
    )

    private fun DecryptionCipherReadyResult.reduce() = copy(
        encryptionFormViewState = encryptionFormViewState.copy(
            inProgress = false
        ),
        errorEvent = null
    ).apply {
        decryptionCipherReadyEvent = ViewStateNonParcelableEvent(decryptionCipher)
    }

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